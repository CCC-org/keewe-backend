package ccc.keeweapi.service.user;

import ccc.keeweapi.config.security.jwt.JwtUtils;
import ccc.keeweapi.component.UserAssembler;
import ccc.keeweapi.dto.user.UserSignUpResponse;
import ccc.keewecore.aop.annotations.FLogging;
import ccc.keewecore.utils.KeeweStringUtils;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.domain.user.enums.VendorType;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.dto.OauthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserApiService {
    private final UserDomainService userDomainService;
    private final UserAssembler userAssembler;
    private final JwtUtils jwtUtils;

    @Transactional
    @FLogging
    public <T extends OauthResponse> UserSignUpResponse signupWithOauth(String code, VendorType vendorType) {
        T account = userDomainService.getOauthProfile(code, vendorType);
        log.info("[UAS::signUp] account {}", account.toString());
        Optional<User> userOps = userDomainService.getUserByVendorIdAndVendorType(account.getId(), vendorType);

        if(userOps.isPresent()) {
            return userAssembler.toUserSignUpResponse(userOps.get(), getToken(userOps.get()));
        }

        User user = signUpWithOauth(
                account.getId()
                , vendorType
                , KeeweStringUtils.getOrDefault(account.getEmail(), "")
        );

        return userAssembler.toUserSignUpResponse(user, getToken(user));
    }

    private User signUpWithOauth(String vendorId, VendorType vendorType, String email) {
        return userDomainService.save(userAssembler.toUserSignUpDto(vendorId, vendorType, email));
    }

    private String getToken(User user) {
        return jwtUtils.createToken(user.getId(), List.of());
    }
}
