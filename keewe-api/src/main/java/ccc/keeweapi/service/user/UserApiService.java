package ccc.keeweapi.service.user;

import ccc.keeweapi.config.security.jwt.JwtUtils;
import ccc.keeweapi.dto.user.UserAssembler;
import ccc.keeweapi.dto.user.UserSignUpResponse;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.dto.UserSignUpDto;
import ccc.keewedomain.service.ProfileDomainService;
import ccc.keewedomain.service.UserDomainService;
import ccc.keeweinfra.dto.KakaoProfileResponse;
import ccc.keeweinfra.vo.Naver.NaverAccount;
import ccc.keeweinfra.vo.kakao.KakaoAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserApiService {
    private final UserDomainService userDomainService;
    private final ProfileDomainService profileDomainService;
    private final UserAssembler userAssembler;
    private final JwtUtils jwtUtils;

    @Transactional
    public UserSignUpResponse signUpWithKakao(String code) {
        KakaoProfileResponse kakaoProfile = userDomainService.getKakaoProfile(code);
        KakaoAccount kakaoAccount = kakaoProfile.getKakaoAccount(); // 이미 Null이 아님을 보장받음

        Optional<User> userOps = userDomainService.getUserByEmail(kakaoAccount.getEmail());
        if(userOps.isPresent()) {
            return userAssembler.toUserSignUpResponse(userOps.get(), getToken(userOps.get()));
        }

        User user = signUpWithOauth(kakaoAccount.getEmail());

        return userAssembler.toUserSignUpResponse(user, getToken(user));
    }

    @Transactional
    public UserSignUpResponse signUpWithNaver(String code) {
        NaverAccount naverAccount = userDomainService.getNaverProfile(code);
        Optional<User> userOps = userDomainService.getUserByEmail(naverAccount.getEmail());

        if(userOps.isPresent()) {
            return userAssembler.toUserSignUpResponse(userOps.get(), getToken(userOps.get()));
        }

        User user = signUpWithOauth(naverAccount.getEmail());

        return userAssembler.toUserSignUpResponse(user, getToken(user));
    }

    private User signUpWithOauth(String email) {
        User user = userDomainService.save(UserSignUpDto.of(email, null, null));
        profileDomainService.createProfile(user);

        return user;
    }

    private String getToken(User user) {
        return jwtUtils.createToken(user.getEmail(), List.of());
    }
}
