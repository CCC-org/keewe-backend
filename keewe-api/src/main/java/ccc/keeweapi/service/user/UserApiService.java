package ccc.keeweapi.service.user;

import ccc.keeweapi.config.security.jwt.JwtUtils;
import ccc.keeweapi.dto.user.UserAssembler;
import ccc.keeweapi.dto.user.UserSignUpResponse;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.dto.UserSignUpDto;
import ccc.keewedomain.service.ProfileDomainService;
import ccc.keewedomain.service.UserDomainService;
import ccc.keeweinfra.dto.KakaoProfileResponse;
import ccc.keeweinfra.vo.OauthAccount;
import ccc.keeweinfra.vo.naver.NaverAccount;
import ccc.keeweinfra.vo.kakao.KakaoAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public <T extends OauthAccount> UserSignUpResponse signupWithOauth(String code, String company) {
        T account = userDomainService.getOauthProfile(code, company);
        Optional<User> userOps = userDomainService.getUserByEmail(account.getEmail());

        if(userOps.isPresent()) {
            return userAssembler.toUserSignUpResponse(userOps.get(), getToken(userOps.get()));
        }

        User user = signUpWithOauth(account.getEmail());

        return userAssembler.toUserSignUpResponse(user, getToken(user));
    }

    private User signUpWithOauth(String email) {
        User user = userDomainService.save(userAssembler.toUserSignUpDto(email));
        profileDomainService.createProfile(user);
        return user;
    }

    private String getToken(User user) {
        return jwtUtils.createToken(user.getEmail(), List.of());
    }
}
