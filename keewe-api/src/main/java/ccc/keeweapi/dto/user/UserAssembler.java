package ccc.keeweapi.dto.user;

import ccc.keewedomain.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler {

    public UserSignUpResponse toUserSignUpResponse(User user, String accessToken) {
        return UserSignUpResponse.of(user.getId(), accessToken);
    }
}
