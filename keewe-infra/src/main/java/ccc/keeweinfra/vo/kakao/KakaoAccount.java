package ccc.keeweinfra.vo.kakao;

import ccc.keeweinfra.vo.OauthAccount;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoAccount implements OauthAccount {
    private Boolean hasEmail;
    private String email;
}