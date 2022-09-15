package ccc.keeweinfra.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Slf4j
public class KakaoProfileResponse implements OauthResponse {
    private String id;
    private String email;

    @JsonCreator
    public KakaoProfileResponse(
            @JsonProperty("id") String id,
            @JsonProperty("kakao_account") Map<String, String> data) {
        this.id = id;
        this.email = data.get("email");
    }
}
