package ccc.keeweapi.service.user;

import ccc.keeweapi.config.security.jwt.JwtUtils;
import ccc.keeweapi.dto.user.UserSignUpDto;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.service.ProfileDomainService;
import ccc.keewedomain.service.UserDomainService;
import ccc.keeweinfra.dto.KakaoProfileResponse;
import ccc.keeweinfra.dto.NaverProfileResponse;
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
    private final JwtUtils jwtUtils;

    @Transactional
    public UserSignUpDto signUpWithKakao(String code) {
        KakaoProfileResponse kakaoProfile = userDomainService.getKakaoProfile(code);
        KakaoAccount kakaoAccount = kakaoProfile.getKakaoAccount(); // 이미 Null이 아님을 보장받음

        Optional<User> userOps = userDomainService.getUserByEmail(kakaoAccount.getEmail());
        if(userOps.isPresent()) {
            return UserSignUpDto.builder()
                    .userId(userOps.get().getId())
                    .accessToken(jwtUtils.createToken(userOps.get().getEmail(), List.of()))
                    .build();
        }

        User user = User.builder().email(kakaoAccount.getEmail()).profiles(new ArrayList<>()).build();
        Profile profile = Profile.init().build();

        profile.connectWithUser(user);

        Long userId = userDomainService.save(user);
        profileDomainService.save(profile);

        return UserSignUpDto.builder()
                .userId(userId)
                .accessToken(jwtUtils.createToken(kakaoAccount.getEmail(), List.of()))
                .build();
    }

    @Transactional
    public UserSignUpDto signUpWithNaver(String code) {
        NaverAccount naverAccount = userDomainService.getNaverProfile(code);
        Optional<User> userOps = userDomainService.getUserByEmail(naverAccount.getEmail());

        if(userOps.isPresent()) {
            return UserSignUpDto.builder()
                    .userId(userOps.get().getId())
                    .accessToken(jwtUtils.createToken(userOps.get().getEmail(), List.of()))
                    .build();
        }

        User user = User.builder().email(naverAccount.getEmail()).profiles(new ArrayList<>()).build();
        Profile profile = Profile.init().build();
        profile.connectWithUser(user);

        Long userId = userDomainService.save(user);
        profileDomainService.save(profile);

        return UserSignUpDto.builder()
                .userId(userId)
                .accessToken(jwtUtils.createToken(naverAccount.getEmail(), List.of()))
                .build();
    }
}
