package ccc.keeweinfra.service;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keeweinfra.apis.GoogleApi;
import ccc.keeweinfra.apis.GoogleAuthApi;
import ccc.keeweinfra.dto.GoogleProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleInfraService {
    private final GoogleApi googleApi;
    private final GoogleAuthApi googleAuthApi;

    public static final String EMPTY_STRING = "";

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.redirect-url}")
    private String redirectUrl;

    @Value("${google.secret}")
    private String clientSecret;

    public String getAccessToken(String code) {
        return googleAuthApi.getAccessToken(code, clientId, clientSecret, redirectUrl, KeeweConsts.AUTH_CODE, EMPTY_STRING).getAccessToken();
    }

    public GoogleProfileResponse getGoogleAccount(String accessToken) {
        return googleApi.getGoogleUser(KeeweConsts.BEARER.concat(" " + accessToken));
    }
}
