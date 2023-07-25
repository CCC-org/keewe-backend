package ccc.keewedomain.service.user.command;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.dto.user.UserTokenRegisterDto;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.domain.user.UserToken;
import ccc.keewedomain.persistence.repository.user.UserRepository;
import ccc.keewedomain.persistence.repository.user.UserTokenRepository;
import ccc.keewedomain.service.user.UserDomainService;
import ccc.keeweinfra.service.image.S3StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCommandDomainService {
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final UserDomainService userDomainService;
    private final S3StoreService storeService;

    @Transactional
    public User withdraw(Long userId) {
        User user = userDomainService.getUserByIdOrElseThrow(userId);
        storeService.delete(user.getProfilePhotoURL());
        user.withdraw();
        return userRepository.save(user);
    }

    @PostConstruct
    public void job() {
        userRepository.findAll().forEach(user -> userTokenRepository.save(UserToken.of(user.getId(), null, null, null)));
    }

    @Transactional
    public UserToken registerToken(UserTokenRegisterDto dto) {
        UserToken userToken = UserToken.of(dto.getUserId(), dto.getAccessToken(), dto.getRefreshToken(), dto.getPushToken());
        return userTokenRepository.save(userToken);
    }

    @Transactional
    public void registerPushToken(Long userId, String pushToken) {
        UserToken userToken = this.findByIdOrElseThrow(userId);
        userToken.registerPushToken(pushToken);
    }

    @Transactional(readOnly = true)
    public UserToken findByIdOrElseThrow(Long userId) {
        return userTokenRepository.findById(userId).orElseThrow(() -> {
            throw new KeeweException(KeeweRtnConsts.ERR413);
        });
    }
}
