package ccc.keeweinfra.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ToString
@NoArgsConstructor
public class NaverTokenResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Integer expiresIn;
    private String error;
    private String errorDescription;
}
