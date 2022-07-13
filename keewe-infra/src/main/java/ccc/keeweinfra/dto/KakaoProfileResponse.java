package ccc.keeweinfra.dto;

import ccc.keeweinfra.vo.kakao.KakaoAccount;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class KakaoProfileResponse {
    private Long id;
    private String connectedAt;
    private KakaoAccount kakaoAccount;
}
