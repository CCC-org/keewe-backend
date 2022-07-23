package ccc.keeweinfra.service;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keeweinfra.apis.NaverApi;
import ccc.keeweinfra.apis.NaverAuthApi;
import ccc.keeweinfra.dto.NaverProfileResponse;
import ccc.keeweinfra.dto.NaverTokenResponse;
import ccc.keeweinfra.vo.naver.NaverAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
        System.out.println(response.toString());
        return response.getAccessToken();
    }

    public NaverAccount getNaverAccount(String accessToken) {
        NaverProfileResponse profile = naverApi.getNaverUser(KeeweConsts.BEARER.concat(" " + accessToken));
        Assert.notNull(profile.getNaverAccount(), KeeweRtnConsts.ERR412.getDescription());
        Assert.notNull(profile.getNaverAccount().getEmail(), KeeweRtnConsts.ERR412.getDescription());
        return profile.getNaverAccount();
    }
}
