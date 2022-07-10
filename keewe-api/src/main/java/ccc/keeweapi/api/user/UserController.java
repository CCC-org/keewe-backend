package ccc.keeweapi.api.user;

import ccc.keeweapi.dto.user.UserSignUpDto;
import ccc.keeweapi.service.user.UserApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserApiService userService;

    @PostMapping("/kakao")
    public UserSignUpDto signUpWithKakao(@RequestParam String code) {
        log.info("[Kakao Signup] code {}", code);
        return userService.signUpWithKakao(code);
    }
}