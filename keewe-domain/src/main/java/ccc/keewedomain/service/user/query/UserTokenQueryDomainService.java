package ccc.keewedomain.service.user.query;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.persistence.domain.user.UserToken;
import ccc.keewedomain.persistence.repository.user.UserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserTokenQueryDomainService {
    private final UserTokenRepository userTokenRepository;

    @Transactional(readOnly = true)
    public UserToken findByIdOrElseThrow(Long userId) {
        return userTokenRepository.findById(userId).orElseThrow(() -> {
            throw new KeeweException(KeeweRtnConsts.ERR413);
        });
    }
}
