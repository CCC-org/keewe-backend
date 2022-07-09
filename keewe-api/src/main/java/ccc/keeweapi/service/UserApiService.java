package ccc.keeweapi.service;

import ccc.keeweapi.config.security.jwt.JwtUtils;
import ccc.keeweapi.dto.UserSignUpDto;
import ccc.keewedomain.service.UserDomainService;
import ccc.keewedomain.user.User;
import ccc.keeweinfra.dto.KakaoProfileResponse;
import ccc.keeweinfra.vo.kakao.KakaoAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserApiService {
    private final UserDomainService userDomainService;
    private final JwtUtils jwtUtils;

    public UserSignUpDto signUpWithKakao(String code) {
        KakaoProfileResponse profile = userDomainService.getKakaoProfile(code);
        KakaoAccount kakaoAccount = profile.getKakaoAccount(); // 이미 Null이 아님을 보장받음

        Optional<User> userOps = userDomainService.getUserByEmail(kakaoAccount.getEmail());
        if(userOps.isPresent()) {
            return UserSignUpDto.builder()
                    .userId(userOps.get().getId())
                    .accessToken(jwtUtils.createToken(userOps.get().getEmail(), List.of()))
                    .build();
        }

        Long id = userDomainService.save(User.builder()
                .email(kakaoAccount.getEmail())
                .build());

        return UserSignUpDto.builder()
                .userId(id)
                .accessToken(jwtUtils.createToken(kakaoAccount.getEmail(), List.of()))
                .build();
    }
}
