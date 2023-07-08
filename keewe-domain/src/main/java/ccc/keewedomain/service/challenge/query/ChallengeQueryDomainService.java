package ccc.keewedomain.service.challenge.query;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.challenge.ChallengeQueryRepository;
import ccc.keewedomain.persistence.repository.challenge.ChallengeRepository;
import ccc.keewedomain.persistence.repository.user.UserRepository;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChallengeQueryDomainService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeQueryRepository challengeQueryRepository;
    private final UserRepository userRepository;

    public Challenge getByIdOrElseThrow(Long id) {
        return challengeRepository.findById(id).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR430));
    }

    public List<Challenge> paginate(CursorPageable<Long> cPage) {
        return challengeQueryRepository.paginate(cPage);
    }

    public Long countByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow();
        return challengeQueryRepository.countCreatedChallengeByUser(user);
    }
}
