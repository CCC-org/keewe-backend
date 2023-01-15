package ccc.keeweinfra.service.oauth;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keeweinfra.apis.oauth.NaverApi;
import ccc.keeweinfra.apis.oauth.NaverAuthApi;
import ccc.keeweinfra.dto.NaverProfileResponse;
import ccc.keeweinfra.dto.NaverTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NaverInfraService {

    private final NaverApi naverApi;
    private final NaverAuthApi naverAuthApi;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.secret}")
    private String clientSecret;

    public String getAccessToken(String code) {
        NaverTokenResponse response = naverAuthApi.getAccessToken(
                KeeweConsts.AUTH_CODE,
                clientId,
                clientSecret,
                code);
        return response.getAccessToken();
    }

    public NaverProfileResponse getNaverAccount(String accessToken) {
        return naverApi.getNaverUser(KeeweConsts.BEARER.concat(" " + accessToken));
    }
}
