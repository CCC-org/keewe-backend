package ccc.keeweapi.component;

import ccc.keeweapi.dto.user.FollowToggleResponse;
import ccc.keeweapi.dto.user.OnboardRequest;
import ccc.keeweapi.dto.user.OnboardResponse;
import ccc.keeweapi.dto.user.ProfileMyPageResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.dto.user.FollowCheckDto;
import ccc.keewedomain.dto.user.FollowToggleDto;
import ccc.keewedomain.dto.user.UploadProfilePhotoDto;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.dto.user.OnboardDto;
import ccc.keewedomain.persistence.domain.common.Interest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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
        return FollowCheckDto.of(targetId, SecurityUtil.getUserId());
    }

    public UploadProfilePhotoDto toUploadProfilePhotoDto(MultipartFile imageFile) {
        return UploadProfilePhotoDto.of(
                SecurityUtil.getUserId(),
                imageFile
        );
    }

    //TODO 프로필 사진, 타이틀, 자기소개 수정
    public ProfileMyPageResponse toProfileMyPageResponse(User user, Boolean isFollowing, Long followerCount, Long followingCount, String challengeName) {
        return ProfileMyPageResponse.of(
                user.getNickname(),
                "image",
                "title",
                "introduction",
                user.getInterests().stream()
                        .map(Interest::getName)
                        .collect(Collectors.toList()),
                isFollowing,
                followerCount,
                followingCount,
                challengeName
        );
    }
}
