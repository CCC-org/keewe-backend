package ccc.keeweapi.controller.api;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keewedomain.persistence.domain.misc.KeeweConfig;
import ccc.keewedomain.persistence.repository.misc.KeeweConfigRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KeeweConfigController {
    private final KeeweConfigRepository keeweConfigRepository;

    @GetMapping("/api/v1/config/allowed-oauth-vendor")
    public ApiResponse<AllowedOauthVendorResponse> getLoginControlInfo() {
        KeeweConfig keeweConfig = keeweConfigRepository.findById("ALLOWED_OAUTH_VENDORS")
                .orElseGet(() -> new KeeweConfig("ALLOWED_OAUTH_VENDORS", "KAKAO,NAVER,GOOGLE,APPLE"));
        List<String> allowedOauthVendors = List.of(keeweConfig.getValue().split(","));
        return ApiResponse.ok(AllowedOauthVendorResponse.of(allowedOauthVendors));
    }

    @Data
    @AllArgsConstructor(staticName = "of")
    public static class AllowedOauthVendorResponse {
        private List<String> vendors;
    }
}
