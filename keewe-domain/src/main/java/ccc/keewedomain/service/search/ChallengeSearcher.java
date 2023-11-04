package ccc.keewedomain.service.search;

import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.repository.challenge.ChallengeQueryRepository;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChallengeSearcher implements Searcher<Challenge> {
    private final ChallengeQueryRepository challengeQueryRepository;

    @Override
    public List<Challenge> search(String keyword, CursorPageable<Long> cPage) {
        return challengeQueryRepository.findAllByKeyword(keyword, cPage);
    }
}
