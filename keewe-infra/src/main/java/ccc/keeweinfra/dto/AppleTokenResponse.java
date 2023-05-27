package ccc.keeweinfra.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class AppleTokenResponse {
    private String accessToken;
    private Integer expiresIn;
    private String refresh_token;
    private String tokenType;
    private String idToken;
}
