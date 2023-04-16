package ccc.keeweapi.service.user;

import static ccc.keewecore.consts.KeeweConsts.MY_PAGE_TITLE_LIMIT;

import ccc.keeweapi.aop.annotations.BlockFilter;
import ccc.keeweapi.component.ProfileAssembler;
import ccc.keeweapi.dto.user.AllAchievedTitleResponse;
import ccc.keeweapi.dto.user.BlockUserResponse;
import ccc.keeweapi.dto.user.FollowToggleResponse;
import ccc.keeweapi.dto.user.FollowUserListResponse;
import ccc.keeweapi.dto.user.InterestsResponse;
import ccc.keeweapi.dto.user.MyBlockUserListResponse;
import ccc.keeweapi.dto.user.MyPageTitleResponse;
import ccc.keeweapi.dto.user.OnboardRequest;
import ccc.keeweapi.dto.user.OnboardResponse;
import ccc.keeweapi.dto.user.ProfileMyPageResponse;
import ccc.keeweapi.dto.user.ProfileUpdateRequest;
import ccc.keeweapi.dto.user.ProfileUpdateResponse;
import ccc.keeweapi.dto.user.UnblockUserResponse;
import ccc.keeweapi.dto.user.UploadProfilePhotoResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.dto.user.FollowCheckDto;
import ccc.keewedomain.persistence.domain.title.TitleAchievement;
import ccc.keewedomain.persistence.domain.user.Block;
import ccc.keewedomain.persistence.domain.user.Follow;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.challenge.query.ChallengeParticipateQueryDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keewedomain.service.user.command.ProfileCommandDomainService;
import ccc.keewedomain.service.user.query.ProfileQueryDomainService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class ProfileApiService {
    private final UserDomainService userDomainService;
    private final ChallengeParticipateQueryDomainService challengeParticipateQueryDomainService;
    private final ProfileAssembler profileAssembler;
    private final ProfileQueryDomainService profileQueryDomainService;
    private final ProfileCommandDomainService profileCommandDomainService;

    @Transactional
    public OnboardResponse onboard(OnboardRequest request) {
        User user = profileCommandDomainService.onboard(profileAssembler.toOnboardDto(request));
        return profileAssembler.toOnboardResponse(user);
    }

    @Transactional
    @BlockFilter
    public FollowToggleResponse toggleFollowership(Long userId) {
        boolean following = profileCommandDomainService.toggleFollowership(profileAssembler.toFollowToggleDto(userId));
        return profileAssembler.toFollowToggleResponse(following);
    }

    @Transactional
    public UploadProfilePhotoResponse uploadProfilePhoto(MultipartFile imageFile) {
        String image = profileCommandDomainService.uploadProfilePhoto(profileAssembler.toUploadProfilePhotoDto(imageFile));
        return UploadProfilePhotoResponse.of(image);
    }

    @Transactional(readOnly = true)
    @BlockFilter
    public ProfileMyPageResponse getMyPageProfile(Long userId) {
        User targetUser = userDomainService.getUserByIdWithInterests(userId);
        Long requestUserId = SecurityUtil.getUserId();

        boolean isFollowing = profileQueryDomainService.isFollowing(FollowCheckDto.of(userId, requestUserId));
        Long followerCount = profileQueryDomainService.getFollowerCount(targetUser);
        Long followingCount = profileQueryDomainService.getFollowingCount(targetUser);

        String challengeName = challengeParticipateQueryDomainService.findCurrentParticipationByUserId(requestUserId)
                .map(challengeParticipation -> challengeParticipation.getChallenge().getName())
                .orElse(null);

        return profileAssembler.toProfileMyPageResponse(targetUser, isFollowing, followerCount, followingCount, challengeName);
    }

    @Transactional(readOnly = true)
    @BlockFilter
    public MyPageTitleResponse getMyPageTitles(Long userId) {
        List<TitleAchievement> titleAchievements = profileQueryDomainService.getTitleAchievements(userId, MY_PAGE_TITLE_LIMIT);
        Long total = profileQueryDomainService.getAchievedTitleCount(userId);

        return profileAssembler.toMyPageTitleResponse(total, titleAchievements);
    }

    @Transactional(readOnly = true)
    @BlockFilter
    public AllAchievedTitleResponse getAllAchievedTitles(Long userId) {
        List<TitleAchievement> titleAchievements = profileQueryDomainService.getTitleAchievements(userId, Integer.MAX_VALUE);

        return profileAssembler.toAllAchievedTitleResponse(SecurityUtil.getUser(), titleAchievements);
    }

    @Transactional(readOnly = true)
    @BlockFilter
    public FollowUserListResponse getFollowers(Long userId, CursorPageable<LocalDateTime> cPage) {
        List<Follow> follows = profileQueryDomainService.getFollowers(userId, cPage);

        return getFollowUser(follows, Follow::getFollower);
    }

    @Transactional(readOnly = true)
    @BlockFilter
    public FollowUserListResponse getFollowees(Long userId, CursorPageable<LocalDateTime> cPage) {
        List<Follow> follows = profileQueryDomainService.getFollowees(userId, cPage);

        return getFollowUser(follows, Follow::getFollowee);
    }

    @Transactional
    public BlockUserResponse blockUser(Long blockedUserId) {
        Long blockUserId = profileCommandDomainService.blockUser(SecurityUtil.getUserId(), blockedUserId);
        return profileAssembler.toBlockUserResponse(blockedUserId);
    }

    private FollowUserListResponse getFollowUser(List<Follow> follows, Function<Follow, User> followUserFunction) {
        List<User> users = follows.stream()
                .map(followUserFunction)
                .collect(Collectors.toList());

        Set<Long> followingIdSet = profileQueryDomainService.getFollowingTargetIdSet(SecurityUtil.getUser(), users);
        return profileAssembler.toFollowUserListResponse(follows, users, followingIdSet);
    }

    @Transactional
    public UnblockUserResponse unblockUser(Long blockedUserId) {
        Long unblockUserId = profileCommandDomainService.unblockUser(SecurityUtil.getUserId(), blockedUserId);
        return profileAssembler.toUnblockUserResponse(unblockUserId);
    }

    @Transactional(readOnly = true)
    public MyBlockUserListResponse getMyBlockList() {
        List<Block> blocks = profileQueryDomainService.findBlocksByUserId(SecurityUtil.getUserId());
        return profileAssembler.toMyBlockUserListResponse(blocks);
    }

    @Transactional
    public ProfileUpdateResponse updateProfile(ProfileUpdateRequest request) {
        User user = profileCommandDomainService.updateProfile(SecurityUtil.getUserId(), profileAssembler.toProfileUpdateDto(request));
        return profileAssembler.toProfileUpdateResponse(user);
    }

    @Transactional(readOnly = true)
    public InterestsResponse getInterests() {
        return profileAssembler.toInterestsResponse(profileQueryDomainService.getInterests(SecurityUtil.getUser()));
    }
}
