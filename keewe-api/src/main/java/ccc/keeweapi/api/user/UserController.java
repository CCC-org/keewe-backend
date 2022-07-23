package ccc.keeweapi.api.user;

import ccc.keeweapi.dto.ApiResponse;
import ccc.keeweapi.service.user.UserApiService;
import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    private final UserApiService userService;

    @Value("${naver.state-name}")
    private String naverState;

    @GetMapping("/kakao")
    public ApiResponse<?> signUpWithKakao(@RequestParam String code) {
        log.info("[Kakao Signup] code {}", code);
        return ApiResponse.ok(userService.signupWithOauth(code, KeeweConsts.KAKAO));
    }

    @GetMapping("/naver")
    public ApiResponse<?> signUpWithNaver(HttpSession session, @RequestParam String code, @RequestParam String state) {
        verifySessionState(session, state);
        log.info("[Naver Signup] code {}, state {}", code, state);
        return ApiResponse.ok(userService.signupWithOauth(code, KeeweConsts.NAVER));
    }

    //FIXME 컨트롤러보다 앞단에서 검증
    private void verifySessionState(HttpSession session, String state) {
        String sessionState = (String) session.getAttribute(naverState);
        if (!state.equals(sessionState)) {
            log.error("session의 state = {} / 전달 받은 state = {}", sessionState, state);
            throw new KeeweException(KeeweRtnConsts.ERR503);
        }
    }
}