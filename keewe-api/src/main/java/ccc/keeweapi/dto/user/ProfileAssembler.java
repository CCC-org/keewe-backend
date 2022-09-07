package ccc.keeweapi.dto.user;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.domain.user.User;
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
}
