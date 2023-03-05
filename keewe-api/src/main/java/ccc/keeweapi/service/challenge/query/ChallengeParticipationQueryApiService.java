package ccc.keeweapi.service.challenge.query;

import ccc.keeweapi.component.ChallengeAssembler;
import ccc.keeweapi.dto.challenge.CompletedChallengeResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.challenge.query.ChallengeParticipateQueryDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChallengeParticipationQueryApiService {
    private final ChallengeParticipateQueryDomainService challengeParticipateQueryDomainService;
    private final ChallengeAssembler challengeAssembler;

    public List<CompletedChallengeResponse> paginateCompleted(CursorPageable<Long> cPage) {
        return challengeParticipateQueryDomainService.paginateCompleted(SecurityUtil.getUser(), cPage).stream()
                .map(challengeAssembler::toCompletedChallengeResponse)
                .collect(Collectors.toList());
    }
}
