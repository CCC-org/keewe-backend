package ccc.keeweapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
public class UserSignUpResponse {
    private Long userId;
    private String accessToken;
}
