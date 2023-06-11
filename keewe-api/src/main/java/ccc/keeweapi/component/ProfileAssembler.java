package ccc.keeweapi.component;

import ccc.keeweapi.dto.insight.response.ProfileVisitFromInsightCountResponse;
import ccc.keeweapi.dto.user.*;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.dto.user.*;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.common.Interest;
import ccc.keewedomain.persistence.domain.title.Title;
import ccc.keewedomain.persistence.domain.title.TitleAchievement;
import ccc.keewedomain.persistence.domain.user.Block;
import ccc.keewedomain.persistence.domain.user.Follow;
import ccc.keewedomain.persistence.domain.user.User;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProfileAssembler {

    public OnboardDto toOnboardDto(OnboardRequest request) {
        return OnboardDto.of(SecurityUtil.getUserId(), request.getNickname(), request.getInterests());
    }

    public OnboardResponse toOnboardResponse(User user) {
        return OnboardResponse.of(user.getId(), user.getNickname(), user.getInterests());
    }

    public FollowToggleDto toFollowToggleDto(Long targetId) {
        return FollowToggleDto.of(SecurityUtil.getUserId(), targetId);
    }

    public FollowToggleResponse toFollowToggleResponse(boolean following) {
        return FollowToggleResponse.of(following);
    }

    public FollowCheckDto toFollowCheckDto(Long targetId) {
        return FollowCheckDto.of(SecurityUtil.getUserId(), targetId);
    }

    public UploadProfilePhotoDto toUploadProfilePhotoDto(MultipartFile imageFile) {
        return UploadProfilePhotoDto.of(
                SecurityUtil.getUserId(),
                imageFile
        );
    }

    public ProfileMyPageResponse toProfileMyPageResponse(User user, Boolean isFollowing, Long followerCount, Long followingCount, Challenge challenge) {
        Long challengeId = challenge != null ? challenge.getId() : null;
        String challengeName = challenge != null ? challenge.getName() : null;
        return ProfileMyPageResponse.of(
                user.getNickname(),
                user.getProfilePhotoURL(),
                user.getRepTitleName(),
                user.getIntroduction(),
                user.getInterests().stream()
                        .map(Interest::getName)
                        .collect(Collectors.toList()),
                isFollowing,
                followerCount,
                followingCount,
                challengeName,
                challengeId
        );
    }

    public MyPageTitleResponse toMyPageTitleResponse(Long total, List<TitleAchievement> achievements) {
        List<AchievedTitleResponse> achievedTitleResponses = achievements.stream()
                .map(this::toAchievedTitleResponse)
                .collect(Collectors.toList());

        return MyPageTitleResponse.of(total, achievedTitleResponses);
    }

    public AchievedTitleResponse toAchievedTitleResponse(TitleAchievement achievement) {
        Title title = achievement.getTitle();
        return AchievedTitleResponse.of(
                title.getId(),
                title.getName(),
                title.getIntroduction(),
                achievement.getCreatedAt()
        );
    }

    public AllAchievedTitleResponse toAllAchievedTitleResponse(User user, List<TitleAchievement> achievements) {
        List<AchievedTitleResponse> achievedTitleResponses = achievements.stream()
                .map(this::toAchievedTitleResponse)
                .collect(Collectors.toList());

        return AllAchievedTitleResponse.of(user.getRepTitleId(), achievedTitleResponses);
    }

    public FollowUserResponse toFollowerResponse(User follower, boolean isFollow) {
        return FollowUserResponse.of(
                follower.getId(),
                follower.getNickname(),
                follower.getProfilePhotoURL(),
                follower.getRepTitleName(),
                isFollow);
    }

    public FollowUserListResponse toFollowUserListResponse(List<Follow> follows, List<User> users, Set<Long> followingIdSet) {
        List<FollowUserResponse> followUserResponse = users.stream()
                .map(user -> this.toFollowerResponse(user, followingIdSet.contains(user.getId())))
                .collect(Collectors.toList());

        LocalDateTime cursor = !follows.isEmpty() ? follows.get(follows.size() - 1).getCreatedAt() : null;
        return FollowUserListResponse.of(Optional.ofNullable(cursor), followUserResponse);
    }

    public BlockUserResponse toBlockUserResponse(Long blockedUserId) {
        return BlockUserResponse.of(blockedUserId);
    }

    public UnblockUserResponse toUnblockUserResponse(Long unblockUserId) {
        return UnblockUserResponse.of(unblockUserId);
    }

    public MyBlockUserListResponse toMyBlockUserListResponse(List<Block> blocks) {
        List<MyBlockUserResponse> myBlockUserResponses = blocks.stream()
                .map(this::toMyBlockUserResponse)
                .collect(Collectors.toList());

        return MyBlockUserListResponse.of(myBlockUserResponses);
    }

    public MyBlockUserResponse toMyBlockUserResponse(Block block) {
        User user = block.getBlockedUser();
        return MyBlockUserResponse.of(user.getId(), user.getNickname(), user.getRepTitleName(), user.getProfilePhotoURL());
    }

    public ProfileUpdateDto toProfileUpdateDto(ProfileUpdateRequest request) {
        return ProfileUpdateDto.of(
                request.getProfileImage(),
                request.getNickname(),
                request.getInterests(),
                request.getRepTitleId(),
                request.getIntroduction(),
                request.isUpdatePhoto()
        );
    }

    public ProfileUpdateResponse toProfileUpdateResponse(User user) {
        return ProfileUpdateResponse.of(user.getProfilePhotoURL());
    }

    public InterestsResponse toInterestsResponse(List<String> interests) {
        return InterestsResponse.of(interests);
    }

    public InviteeResponse toRelatedUserResponse(User user) {
        return InviteeResponse.of(user.getId(), user.getNickname(), user.getProfilePhotoURL());
    }

    public InviteeListResponse toInviteeListResponse(List<User> invitees, String nextCursor) {
        List<InviteeResponse> inviteeResponse = invitees.stream()
                .map(this::toRelatedUserResponse)
                .collect(Collectors.toList());

        return InviteeListResponse.of(nextCursor, inviteeResponse);
    }

    public AccountResponse toAccountResponse(User user) {
        return AccountResponse.of(user.getVendorType(), user.getIdentifier());
    }

    public FollowFromInsightCountResponse toFollowFromInsightCountResponse(Long followFromInsightCount) {
        return FollowFromInsightCountResponse.of(followFromInsightCount);
    }

    public ProfileVisitFromInsightCountResponse toProfileVisitFromInsightCountResponse(Long profileVisitFromInsightCount) {
        return ProfileVisitFromInsightCountResponse.of(profileVisitFromInsightCount);
    }

    public InviteeSearchResponse toInviteeSearchResponse(List<User> invitees, String nextCursor) {
        List<InviteeResponse> inviteeResponse = invitees.stream()
                .map(this::toRelatedUserResponse)
                .collect(Collectors.toList());
        return InviteeSearchResponse.of(nextCursor, inviteeResponse);
    }
}