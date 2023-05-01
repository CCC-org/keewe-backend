package ccc.keewedomain.service.challenge.query;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.repository.challenge.ChallengeQueryRepository;
import ccc.keewedomain.persistence.repository.challenge.ChallengeRepository;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChallengeQueryDomainService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeQueryRepository challengeQueryRepository;

    public Challenge getByIdOrElseThrow(Long id) {
        return challengeRepository.findById(id).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR430));
    }

    public List<Challenge> paginate(CursorPageable<Long> cPage) {
        return challengeQueryRepository.paginate(cPage);
    }
}
