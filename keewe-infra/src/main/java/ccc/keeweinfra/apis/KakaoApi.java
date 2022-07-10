package ccc.keeweinfra.apis;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keeweinfra.dto.KakaoProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakao", url="${kakao.url}")
public interface KakaoApi {
    @GetMapping(value = "/v2/user/me", consumes = "application/x-www-form-urlencoded;charset=utf-8")
    KakaoProfileResponse getKakaoUser(@RequestHeader(name = KeeweConsts.AUTH_HEADER) String accessToken);
}
