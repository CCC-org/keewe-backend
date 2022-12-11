package ccc.keewedomain.service.user;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.domain.user.enums.VendorType;
import ccc.keewedomain.dto.user.UserSignUpDto;
import ccc.keewedomain.persistence.repository.user.UserQueryRepository;
import ccc.keewedomain.persistence.repository.user.UserRepository;
import ccc.keeweinfra.dto.GoogleProfileResponse;
import ccc.keeweinfra.dto.KakaoProfileResponse;
import ccc.keeweinfra.dto.NaverProfileResponse;
import ccc.keeweinfra.dto.OauthResponse;
import ccc.keeweinfra.service.GoogleInfraService;
import ccc.keeweinfra.service.KakaoInfraService;
import ccc.keeweinfra.service.NaverInfraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDomainService {
    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;
    private final KakaoInfraService kakaoInfraService;
    private final NaverInfraService naverInfraService;
    private final GoogleInfraService googleInfraService;

    public <T extends OauthResponse> T getOauthProfile(String code, VendorType vendorType) {

        switch (vendorType) {
            case KAKAO:
                return (T) getKakaoProfile(code);
            case NAVER:
                return (T) getNaverProfile(code);
            case GOOGLE:
                return (T) getGoogleProfile(code);
            default:
                throw new KeeweException(KeeweRtnConsts.ERR504);
        }

    }

    public User save(UserSignUpDto userSignUpDto) {
        return userRepository.save(User.from(userSignUpDto));
    }

    public User getUserByIdOrElseThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR411));
    }

    public Optional<User> getUserByVendorIdAndVendorType(String vendorId, VendorType vendorType) {
        return userRepository.findByVendorIdAndVendorType(vendorId, vendorType);
    }

    public User getUserByIdWithInterests(Long id) {
        return userQueryRepository.findByIdWithInterests(id).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR411));
    }

    private KakaoProfileResponse getKakaoProfile(String code) {
        try {
            return kakaoInfraService.getKakaoAccount(kakaoInfraService.getAccessToken(code));
        } catch (Exception e) {
            log.error("[getKakaoProfile] fail {}", e.getMessage());
            throw new KeeweException(KeeweRtnConsts.ERR501);
        }
    }

    private NaverProfileResponse getNaverProfile(String code) {
        try {
            return naverInfraService.getNaverAccount(naverInfraService.getAccessToken(code));
        } catch (Exception e) {
            log.error("[getNaverProfile] fail {}", e.getMessage());
            throw new KeeweException(KeeweRtnConsts.ERR502);
        }
    }

    private GoogleProfileResponse getGoogleProfile(String code) {
        try {
            return googleInfraService.getGoogleAccount(googleInfraService.getAccessToken(code));
        } catch (Exception e) {
            log.error("[getGoogleProfile] fail {}", e.getMessage());
            throw new KeeweException(KeeweRtnConsts.ERR505);
        }
    }

}
