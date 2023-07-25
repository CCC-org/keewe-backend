package ccc.keewedomain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class UserTokenRegisterDto {
    private Long userId;
    private String accessToken;
    private String refreshToken;
    private String pushToken;
}
