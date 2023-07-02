package ccc.keeweapi.controller.api.user;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.dto.user.AppleSignUpRequest;
import ccc.keeweapi.service.user.UserApiService;
import ccc.keewecore.aop.annotations.LocalOnlyApi;
import ccc.keewedomain.persistence.domain.user.enums.VendorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserSignUpController {

    private final UserApiService userService;

    @GetMapping("/kakao")
    public ApiResponse<?> signUpWithKakao(@RequestParam String code) {
        return ApiResponse.ok(userService.signupWithOauth(code, VendorType.KAKAO));
    }

    @GetMapping("/naver")
    public ApiResponse<?> signUpWithNaver(@RequestParam String code, @RequestParam String state) {
        return ApiResponse.ok(userService.signupWithOauth(code, VendorType.NAVER));
    }

    @GetMapping("/google")
    public ApiResponse<?> signUpWithGoogle(@RequestParam String code) {
        return ApiResponse.ok(userService.signupWithOauth(code, VendorType.GOOGLE));
    }

    @GetMapping("/apple")
    public ApiResponse<?> signUpWithApple(@RequestParam String code) {
        log.info("[Apple Signup] code {}", code);
        return ApiResponse.ok(userService.signupWithOauth(code, VendorType.APPLE));
    }

    @GetMapping("/force-signup/{userId}")
    @LocalOnlyApi
    public ApiResponse<?> getToken(@PathVariable Long userId) {
        return ApiResponse.ok(userService.getToken(userId));
    }

    @GetMapping("/virtual")
    @LocalOnlyApi
    public ApiResponse<?> signUpWithVirtualVendor(@RequestParam String code) {
        return ApiResponse.ok(userService.signupWithOauth(code, VendorType.VIRTUAL));
    }

    @PutMapping("/withdraw")
    public ApiResponse<Void> withdraw() {
        userService.withdraw();
        return ApiResponse.ok();
    }
}