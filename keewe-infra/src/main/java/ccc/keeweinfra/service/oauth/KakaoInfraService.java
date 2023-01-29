package ccc.keeweinfra.service.oauth;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keeweinfra.apis.oauth.KakaoApi;
import ccc.keeweinfra.apis.oauth.KakaoAuthApi;
import ccc.keeweinfra.dto.KakaoProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoInfraService {
    private final KakaoApi kakaoApi;
    private final KakaoAuthApi kakaoAuthApi;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-url}")
    private String redirectUrl;

    @Value("${kakao.secret}")
    private String clientSecret;

    public String getAccessToken(String code) {
        return kakaoAuthApi.getAccessToken(KeeweConsts.AUTH_CODE, clientId, redirectUrl, code, clientSecret).getAccessToken();
    }

    public KakaoProfileResponse getKakaoAccount(String accessToken) {
        return kakaoApi.getKakaoUser(KeeweConsts.BEARER.concat(" " + accessToken));
    }

}
