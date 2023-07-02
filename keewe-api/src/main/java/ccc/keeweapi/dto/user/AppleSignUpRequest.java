package ccc.keeweapi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class AppleSignUpRequest {
    private String code;
}
