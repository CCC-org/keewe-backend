package ccc.keeweinfra.apis.oauth;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keeweinfra.dto.GoogleProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "google", url = "${google.url}")
public interface GoogleApi {
    @GetMapping(value = "/oauth2/v1/userinfo")
    GoogleProfileResponse getGoogleUser(@RequestHeader(name = KeeweConsts.AUTH_HEADER) String accessToken);
}
