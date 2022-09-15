package ccc.keeweapi.dto.user;

import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.domain.user.enums.VendorType;
import ccc.keewedomain.dto.user.UserSignUpDto;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler {

    public UserSignUpResponse toUserSignUpResponse(User user, String accessToken) {
        return UserSignUpResponse.of(user.getId(), accessToken);
    }

    public UserSignUpDto toUserSignUpDto(String vendorId, VendorType vendorType, String email) {
        return UserSignUpDto.of(vendorId, vendorType, email, null, null);
    }
}
