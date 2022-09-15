package ccc.keewedomain.dto.user;

import ccc.keewedomain.domain.user.enums.VendorType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class UserSignUpDto {
    private String vendorId;
    private VendorType vendorType;
    private String email;
    private String password;
    private String phoneNumber;
}
