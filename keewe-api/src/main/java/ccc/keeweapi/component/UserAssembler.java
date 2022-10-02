package ccc.keeweapi.component;

import ccc.keeweapi.dto.user.UserSignUpResponse;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.domain.user.enums.VendorType;
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
