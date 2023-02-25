package ccc.keeweapi.controller.api.user;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.service.user.UserApiService;
import ccc.keewecore.aop.annotations.LocalOnlyApi;
import ccc.keewedomain.persistence.domain.user.enums.VendorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserSignUpController {

    private final UserApiService userService;

    @GetMapping("/kakao")
    public ApiResponse<?> signUpWithKakao(@RequestParam String code) {
        log.info("[Kakao Signup] code {}", code);
        return ApiResponse.ok(userService.signupWithOauth(code, VendorType.KAKAO));
    }

    @GetMapping("/naver")
    public ApiResponse<?> signUpWithNaver(@RequestParam String code, @RequestParam String state) {
        log.info("[Naver Signup] code {}, state {}", code, state);
        return ApiResponse.ok(userService.signupWithOauth(code, VendorType.NAVER));
    }

    @GetMapping("/google")
    public ApiResponse<?> signUpWithGoogle(@RequestParam String code) {
        log.info("[Google Signup] code {}", code);
        return ApiResponse.ok(userService.signupWithOauth(code, VendorType.GOOGLE));
    }

    @GetMapping("/force-signup/{userId}")
    @LocalOnlyApi
    public ApiResponse<?> getToken(@PathVariable Long userId) {
        return ApiResponse.ok(userService.getToken(userId));
    }
}