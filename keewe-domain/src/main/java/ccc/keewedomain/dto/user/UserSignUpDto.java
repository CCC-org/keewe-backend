package ccc.keewedomain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class UserSignUpDto {
    private String email;
    private String password;
    private String phoneNumber;
}
