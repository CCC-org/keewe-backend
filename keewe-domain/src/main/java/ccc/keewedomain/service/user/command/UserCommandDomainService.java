package ccc.keewedomain.service.user.command;

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

    @Transactional
    public UserToken registerToken(UserTokenRegisterDto dto) {
        UserToken userToken = UserToken.of(dto.getUserId(), dto.getAccessToken(), dto.getRefreshToken(), dto.getPushToken());
        return userTokenRepository.save(userToken);
    }
}
