package ccc.keeweapi.dto.user;

import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.dto.UserSignUpDto;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler {

    public UserSignUpResponse toUserSignUpResponse(User user, String accessToken) {
        return UserSignUpResponse.of(user.getId(), accessToken);
    }

    public UserSignUpDto toUserSignUpDto(String email) {
        return UserSignUpDto.of(email, null, null);
    }
}
