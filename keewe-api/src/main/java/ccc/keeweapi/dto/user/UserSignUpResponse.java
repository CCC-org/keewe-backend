package ccc.keeweapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class UserSignUpResponse {
    private Long userId;
    private boolean isAlreadySignedUp;
    private String accessToken;
}
