package ccc.keeweinfra.service;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keeweinfra.apis.KakaoApi;
import ccc.keeweinfra.apis.KakaoAuthApi;
import ccc.keeweinfra.dto.KakaoProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Optional;

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

    public KakaoProfileResponse getKakaoProfile(String accessToken) {
        KakaoProfileResponse profile = kakaoApi.getKakaoUser(KeeweConsts.BEARER.concat(" " + accessToken));
        Assert.notNull(profile.getKakaoAccount(), KeeweRtnConsts.ERR412.getDescription());
        Assert.notNull(profile.getKakaoAccount().getEmail(), KeeweRtnConsts.ERR412.getDescription());
        return profile;
    }

}