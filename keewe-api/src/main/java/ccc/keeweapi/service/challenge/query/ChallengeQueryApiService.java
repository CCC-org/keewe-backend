package ccc.keeweapi.service.challenge.query;

import ccc.keeweapi.component.ChallengeAssembler;
import ccc.keeweapi.dto.challenge.OpenedChallengeResponse;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.challenge.query.ChallengeQueryDomainService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ccc.keewedomain.service.insight.InsightDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChallengeQueryApiService {

    private final ChallengeAssembler challengeAssembler;
    private final ChallengeQueryDomainService challengeQueryDomainService;
    private final InsightDomainService insightDomainService;

    public List<OpenedChallengeResponse> paginate(CursorPageable<Long> cPage) {
        List<Challenge> challenges = challengeQueryDomainService.paginate(cPage);
        Map<Long, Long> insightCountPerChallengeMap = insightDomainService.getInsightCountPerChallenge(challenges);
        return challenges.stream()
                .map(challenge -> challengeAssembler.toOpenedChallengeResponse(challenge, insightCountPerChallengeMap.getOrDefault(challenge.getId(), 0L)))
                .collect(Collectors.toList());
    }
}
