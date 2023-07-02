package ccc.keeweinfra.service.oauth;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keeweinfra.apis.oauth.AppleAuthApi;
import ccc.keeweinfra.dto.AppleProfileResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppleInfraService {

    private final AppleAuthApi appleAuthApi;

    @Value("${apple.apple-key}")
    private String appleKey;

    @Value("${apple.apple-key-id}")
    private String appleKeyId;

    @Value("${apple.apple-team-id}")
    private String appleTeamId;

    @Value("${apple.apple-url}")
    private String appleUrl;

    @Value("${apple.apple-pem-value}")
    private String pemValue;

    private PrivateKey privateKey;

    @PostConstruct
    private void loadPrivateKey() {
        try {
            privateKey = createPrivateKey();
        } catch (IOException e) {
            throw new IllegalStateException("애플 pem key에 문제가 있습니다.");
        }
    }

    public String getIdToken(String code) {
        String clientSecret = createClientSecret(privateKey);
        return appleAuthApi.getAccessToken(code, appleKey, clientSecret, KeeweConsts.AUTH_CODE).getIdToken();
    }

    public AppleProfileResponse getAppleAccount(String idToken) {
        String payload = idToken.split("\\.")[1];
        String decodedPayload = new String(Base64.getDecoder().decode(payload), StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonPayload;
        try {
            jsonPayload = mapper.readValue(decodedPayload, Map.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("idToken을 파싱하던 도중 오류가 발생했습니다.");
        }

        String userId = (String) jsonPayload.get("sub");
        String email = (String) jsonPayload.get("email");
        return new AppleProfileResponse(userId, email);
    }

    private PrivateKey createPrivateKey() throws IOException {
        Reader reader = new StringReader(pemValue);
        PEMParser pemParser = new PEMParser(reader);
        JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter();
        PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemParser.readObject();
        return jcaPEMKeyConverter.getPrivateKey(privateKeyInfo);
    }

    private String createClientSecret(PrivateKey privateKey) {
        Map<String, Object> headerParamsMap = new HashMap<>();
        headerParamsMap.put("kid", appleKeyId);
        headerParamsMap.put("alg", "ES256");

        return Jwts
                .builder()
                .setHeaderParams(headerParamsMap)
                .setIssuer(appleTeamId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 30)) // 만료 시간 (30초)
                .setAudience(appleUrl)
                .setSubject(appleKey)
                .signWith(SignatureAlgorithm.ES256, privateKey)
                .compact();
    }
}
