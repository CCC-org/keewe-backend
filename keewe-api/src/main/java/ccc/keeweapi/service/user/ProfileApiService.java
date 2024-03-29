package ccc.keeweapi.service.user;

import ccc.keeweapi.component.ProfileAssembler;
import ccc.keeweapi.dto.user.AccountResponse;
import ccc.keeweapi.dto.user.AllAchievedTitleResponse;
import ccc.keeweapi.dto.user.BlockUserResponse;
import ccc.keeweapi.dto.user.FollowToggleResponse;
import ccc.keeweapi.dto.user.FollowUserListResponse;
import ccc.keeweapi.dto.user.InterestsResponse;
import ccc.keeweapi.dto.user.InviteeListResponse;
import ccc.keeweapi.dto.user.InviteeSearchResponse;
import ccc.keeweapi.dto.user.MyBlockUserListResponse;
import ccc.keeweapi.dto.user.MyPageTitleResponse;
import ccc.keeweapi.dto.user.OnboardRequest;
import ccc.keeweapi.dto.user.OnboardResponse;
import ccc.keeweapi.dto.user.ProfileMyPageResponse;
import ccc.keeweapi.dto.user.ProfileUpdateRequest;
import ccc.keeweapi.dto.user.ProfileUpdateResponse;
import ccc.keeweapi.dto.user.UnblockUserResponse;
import ccc.keeweapi.dto.user.UploadProfilePhotoResponse;
import ccc.keeweapi.utils.BlockedResourceManager;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewecore.utils.ListUtils;
import ccc.keewedomain.dto.user.FollowCheckDto;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.title.TitleAchievement;
import ccc.keewedomain.persistence.domain.user.Block;
import ccc.keewedomain.persistence.domain.user.Follow;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.user.cursor.InviteeSearchCursor;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.challenge.query.ChallengeParticipateQueryDomainService;
import ccc.keewedomain.service.insight.command.InsightStatisticsCommandDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keewedomain.service.user.command.ProfileCommandDomainService;
import ccc.keewedomain.service.user.query.ProfileQueryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ccc.keewecore.consts.KeeweConsts.MY_PAGE_TITLE_LIMIT;


@Service
@RequiredArgsConstructor
public class ProfileApiService {
    private final UserDomainService userDomainService;
    private final ChallengeParticipateQueryDomainService challengeParticipateQueryDomainService;
    private final ProfileAssembler profileAssembler;
    private final ProfileQueryDomainService profileQueryDomainService;
    private final ProfileCommandDomainService profileCommandDomainService;
    private final BlockedResourceManager blockedResourceManager;
    private final InsightStatisticsCommandDomainService insightStatisticsCommandDomainService;

    @Transactional
    public OnboardResponse onboard(OnboardRequest request) {
        User user = profileCommandDomainService.onboard(profileAssembler.toOnboardDto(request));
        return profileAssembler.toOnboardResponse(user);
    }

    public FollowToggleResponse toggleFollowership(Long userId, Long insightId) {
        blockedResourceManager.validateAccessibleUser(userId);
        boolean following = profileCommandDomainService.toggleFollowership(profileAssembler.toFollowToggleDto(userId), insightId);
        return profileAssembler.toFollowToggleResponse(following);
    }

    @Transactional
    public UploadProfilePhotoResponse uploadProfilePhoto(MultipartFile imageFile) {
        String image = profileCommandDomainService.uploadProfilePhoto(profileAssembler.toUploadProfilePhotoDto(imageFile));
        return UploadProfilePhotoResponse.of(image);
    }

    @Transactional(readOnly = true)
    public ProfileMyPageResponse getMyPageProfile(Long userId, Long insightId) {
        blockedResourceManager.validateAccessibleUser(userId);
        User targetUser = userDomainService.getUserByIdWithInterests(userId);
        if (targetUser.isDeleted()) {
            throw new KeeweException(KeeweRtnConsts.ERR429);
        }
        Long requestUserId = SecurityUtil.getUserId();

        boolean isFollowing = profileQueryDomainService.isFollowing(FollowCheckDto.of(requestUserId, targetUser.getId()));
        Long followerCount = profileQueryDomainService.getFollowerCount(targetUser);
        Long followingCount = profileQueryDomainService.getFollowingCount(targetUser);

        Challenge challenge = challengeParticipateQueryDomainService.findCurrentParticipationByUserId(userId)
                .map(ChallengeParticipation::getChallenge)
                .orElse(null);

        afterGetMyProfile(userId, insightId);
        return profileAssembler.toProfileMyPageResponse(targetUser, isFollowing, followerCount, followingCount, challenge);
    }

    @Transactional(readOnly = true)
    public MyPageTitleResponse getMyPageTitles(Long userId) {
        blockedResourceManager.validateAccessibleUser(userId);
        List<TitleAchievement> titleAchievements = profileQueryDomainService.getTitleAchievements(userId, MY_PAGE_TITLE_LIMIT);
        Long total = profileQueryDomainService.getAchievedTitleCount(userId);

        return profileAssembler.toMyPageTitleResponse(total, titleAchievements);
    }

    @Transactional(readOnly = true)
    public AllAchievedTitleResponse getAllAchievedTitles(Long userId) {
        blockedResourceManager.validateAccessibleUser(userId);
        List<TitleAchievement> titleAchievements = profileQueryDomainService.getTitleAchievements(userId, Integer.MAX_VALUE);

        return profileAssembler.toAllAchievedTitleResponse(SecurityUtil.getUser(), titleAchievements);
    }

    @Transactional(readOnly = true)
    public FollowUserListResponse getFollowers(Long userId, CursorPageable<LocalDateTime> cPage) {
        blockedResourceManager.validateAccessibleUser(userId);
        List<Follow> follows = profileQueryDomainService.getFollowers(userId, cPage);

        return getFollowUser(follows, Follow::getFollower);
    }

    @Transactional(readOnly = true)
    public FollowUserListResponse getFollowees(Long userId, CursorPageable<LocalDateTime> cPage) {
        blockedResourceManager.validateAccessibleUser(userId);
        List<Follow> follows = profileQueryDomainService.getFollowees(userId, cPage);

        return getFollowUser(follows, Follow::getFollowee);
    }

    @Transactional
    public BlockUserResponse blockUser(Long blockedUserId) {
        Long blockUserId = profileCommandDomainService.blockUser(SecurityUtil.getUserId(), blockedUserId);
        return profileAssembler.toBlockUserResponse(blockUserId);
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

    @Transactional(readOnly = true)
    public InviteeListResponse paginateInvitees(CursorPageable<LocalDateTime> cPage) {
        Long userId = SecurityUtil.getUserId();
        List<Follow> relatedFollows = profileQueryDomainService.findRelatedFollows(userId, cPage);
        List<User> invitees = getInvitees(userId, relatedFollows);
        String nextCursor = relatedFollows.size() == cPage.getLimit()
                ? ListUtils.getLast(relatedFollows).getCreatedAt().toString()
                : null;
        return profileAssembler.toInviteeListResponse(invitees, nextCursor);
    }

    @Transactional(readOnly = true)
    public InviteeSearchResponse searchInvitees(String searchWord, CursorPageable<InviteeSearchCursor> cPage) {
        Long userId = SecurityUtil.getUserId();
        List<Follow> searchedFollows = profileQueryDomainService.searchRelatedUsers(userId, searchWord, cPage);
        List<User> invitees = getInvitees(userId, searchedFollows);
        invitees.sort((user1, user2) -> {
            int nicknameCompareResult = user1.getNickname().compareTo(user2.getNickname());
            if (nicknameCompareResult != 0) {
                return nicknameCompareResult;
            }
            if (user1.getId() < user2.getId()) {
                return -1;
            }
            return 1;
        });
        if (searchedFollows.size() == cPage.getLimit()) {
            User last = ListUtils.getLast(invitees);
            InviteeSearchCursor cursor = InviteeSearchCursor.of(last.getNickname(), last.getId());
            return profileAssembler.toInviteeSearchResponse(invitees, cursor.toString());
        }
        return profileAssembler.toInviteeSearchResponse(invitees);
    }

    public AccountResponse getAccount() {
        User user = SecurityUtil.getUser();
        return profileAssembler.toAccountResponse(user);
    }

    private void afterGetMyProfile(Long userId, Long insightId) {
        if(insightId != null) {
            insightStatisticsCommandDomainService.publishProfileVisitFromInsightEvent(userId, insightId);
        }
    }

    private List<User> getInvitees(Long userId, List<Follow> searchedFollows) {
        ChallengeParticipation challengeParticipation = challengeParticipateQueryDomainService.getCurrentParticipationByUserId(userId);
        Challenge challenge = challengeParticipation.getChallenge();
        // note. 챌린지에 참여중인 유저는 검색 결과 필터링
        List<Long> challengingUserIds = challenge.getParticipationList()
                .stream()
                .map(it -> it.getChallenger().getId())
                .collect(Collectors.toList());
        return searchedFollows.stream()
                .map(follow -> {
                    if (follow.getFollower().getId().equals(userId)) { // follower가 나인 경우
                        return follow.getFollowee();
                    } else { // followee가 나인 경우
                        return follow.getFollower();
                    }
                })
                .filter(user -> !challengingUserIds.contains(user.getId()))
                .filter(user -> !user.isDeleted())
                .distinct() // 양방향으로 팔로우 되어 있는 경우 중복 제거
                .collect(Collectors.toList());
    }
}
