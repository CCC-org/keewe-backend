package ccc.keeweinfra.apis;

import ccc.keewecore.consts.KeeweConsts;
import ccc.keeweinfra.dto.NaverProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "naver", url = "${naver.url}")
public interface NaverApi {

    @GetMapping(value = "/v1/nid/me", consumes = "application/x-www-form-urlencoded;charset=utf-8")
    NaverProfileResponse getNaverUser(@RequestHeader(name = KeeweConsts.AUTH_HEADER) String accessToken);

}
