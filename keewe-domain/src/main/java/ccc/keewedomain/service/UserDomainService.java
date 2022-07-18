package ccc.keewedomain.service;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.dto.UserSignUpDto;
import ccc.keewedomain.repository.user.UserRepository;
import ccc.keeweinfra.dto.KakaoProfileResponse;
import ccc.keeweinfra.dto.NaverProfileResponse;
import ccc.keeweinfra.service.KakaoInfraService;
import ccc.keeweinfra.service.NaverInfraService;
import ccc.keeweinfra.vo.Naver.NaverAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDomainService {
    private final UserRepository userRepository;
    private final KakaoInfraService kakaoInfraService;
    private final NaverInfraService naverInfraService;

    public KakaoProfileResponse getKakaoProfile(String code) {
        try {
            return kakaoInfraService.getKakaoProfile(kakaoInfraService.getAccessToken(code));
        } catch (Exception e) {
            log.error("[getKakaoProfile] fail {}", e.getMessage());
            throw new KeeweException(KeeweRtnConsts.ERR501);
        }
    }

    public NaverAccount getNaverProfile(String code) {
        try {
            return naverInfraService.getNaverAccount(naverInfraService.getAccessToken(code));
        } catch (Exception e) {
            log.error("[getNaverProfile] fail {}", e.getMessage());
            throw new KeeweException(KeeweRtnConsts.ERR502);
        }

    }

    public Long save(User user) {
        return userRepository.save(user).getId();
    }

    public User save(UserSignUpDto userSignUpDto) {
        return User.from(userSignUpDto);
    }

    public User getUserByEmailOrElseThrow(String email) {
        return userRepository.findByEmailOrElseThrow(email);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
