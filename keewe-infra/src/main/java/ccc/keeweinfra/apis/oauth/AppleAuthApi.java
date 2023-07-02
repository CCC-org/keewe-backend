package ccc.keeweinfra.apis.oauth;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keeweinfra.dto.AppleTokenResponse;
import ccc.keeweinfra.dto.GoogleProfileResponse;
import ccc.keeweinfra.dto.GoogleTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "apple", url = "${apple.auth-url}")
public interface AppleAuthApi {
    @PostMapping("/auth/token")
    AppleTokenResponse getAccessToken(
            @RequestParam("code") String code,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("grant_type") String grantType
    );
}
