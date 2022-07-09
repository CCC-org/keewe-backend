package ccc.keewedomain.service;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.repository.UserRepository;
import ccc.keewedomain.user.User;
import ccc.keeweinfra.dto.KakaoProfileResponse;
import ccc.keeweinfra.service.KakaoInfraService;
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

    public KakaoProfileResponse getKakaoProfile(String code) {
        try {
            return kakaoInfraService.getKakaoProfile(kakaoInfraService.getAccessToken(code));
        } catch (Exception e) {
            log.error("[getKakaoProfile] fail {}", e.getMessage());
            throw new KeeweException(KeeweRtnConsts.ERR501);
        }
    }

    public Long save(User user) {
        return userRepository.save(user).getId();
    }

    public User getUserByEmailOrElseThrow(String email) {
        return userRepository.findByEmailOrElseThrow(email);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
