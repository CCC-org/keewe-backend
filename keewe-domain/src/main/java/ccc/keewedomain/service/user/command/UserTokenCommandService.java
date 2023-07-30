package ccc.keewedomain.service.user.command;

import ccc.keewedomain.persistence.domain.user.UserToken;
import ccc.keewedomain.service.user.query.UserTokenQueryDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserTokenCommandService {
    private final UserTokenQueryDomainService userTokenQueryDomainService;

    @Transactional
    public void registerPushToken(Long userId, String pushToken) {
        UserToken userToken = userTokenQueryDomainService.findByIdOrElseThrow(userId);
        userToken.registerPushToken(pushToken);
    }
}
