package ccc.keeweinfra.service;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keeweinfra.apis.KakaoApi;
import ccc.keeweinfra.apis.KakaoAuthApi;
import ccc.keeweinfra.dto.KakaoProfileResponse;
import ccc.keeweinfra.vo.kakao.KakaoAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


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

    public KakaoAccount getKakaoAccount(String accessToken) {
        KakaoProfileResponse profile = kakaoApi.getKakaoUser(KeeweConsts.BEARER.concat(" " + accessToken));
        Assert.notNull(profile.getKakaoAccount(), KeeweRtnConsts.ERR412.getDescription());
        Assert.notNull(profile.getKakaoAccount().getEmail(), KeeweRtnConsts.ERR412.getDescription());
        return profile.getKakaoAccount();
    }

}
