package ccc.keeweapi.component;

import ccc.keeweapi.dto.user.FollowToggleResponse;
import ccc.keeweapi.dto.user.OnboardRequest;
import ccc.keeweapi.dto.user.OnboardResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.dto.user.FollowToggleDto;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.dto.user.OnboardDto;
import org.springframework.stereotype.Component;

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
}
