package ccc.keeweapi.service.challenge.query;

import ccc.keeweapi.component.ChallengeAssembler;
import ccc.keeweapi.dto.challenge.FinishedChallengeCountResponse;
import ccc.keeweapi.dto.challenge.FinishedChallengeResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.persistence.domain.user.User;
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

    public List<FinishedChallengeResponse> paginateFinished(CursorPageable<Long> cPage) {
        return challengeParticipateQueryDomainService.paginateFinished(SecurityUtil.getUser(), cPage).stream()
                .map(challengeAssembler::toFinishedChallengeResponse)
                .collect(Collectors.toList());
    }

    public FinishedChallengeCountResponse countFinished() {
        User user = SecurityUtil.getUser();
        Long count = challengeParticipateQueryDomainService.countFinishedParticipation(user);
        return challengeAssembler.toFinishedChallengeCountResponse(count);
    }
}
