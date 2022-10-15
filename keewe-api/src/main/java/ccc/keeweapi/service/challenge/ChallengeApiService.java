package ccc.keeweapi.service.challenge;

import ccc.keeweapi.component.ChallengeAssembler;
import ccc.keeweapi.dto.challenge.*;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.service.challenge.ChallengeDomainService;
import ccc.keewedomain.service.insight.InsightDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChallengeApiService {
    private final ChallengeDomainService challengeDomainService;
    private final InsightDomainService insightDomainService;
    private final ChallengeAssembler challengeAssembler;

    @Transactional
    public ChallengeCreateResponse createChallenge(ChallengeCreateRequest request) {
        Challenge challenge = challengeDomainService.save(challengeAssembler.toChallengeCreateDto(request));
        ChallengeParticipation participation = challengeDomainService
                .participate(challengeAssembler.toChallengeParticipateDto(request.getParticipate(), challenge.getId()));
        return challengeAssembler.toChallengeCreateResponse(challenge, participation);
    }

    @Transactional
    public ChallengeParticipationResponse participate(ChallengeParticipateRequest request) {
        ChallengeParticipation participation = challengeDomainService
                .participate(challengeAssembler.toChallengeParticipateDto(request));
        return challengeAssembler.toChallengeParticipationResponse(participation);
    }

    @Transactional(readOnly = true)
    public ParticipationCheckResponse checkParticipation() {
        boolean participation = challengeDomainService.checkParticipation(SecurityUtil.getUserId());
        return challengeAssembler.toParticipationCheckResponse(participation);
    }

    @Transactional(readOnly = true)
    public ParticipationProgressResponse getMyParticipationProgress() {
        Long userId = SecurityUtil.getUserId();
        ChallengeParticipation participation =
                challengeDomainService.findCurrentParticipationWithChallenge(userId).orElse(null);

        if (Objects.isNull(participation)) {
            return null;
        }

        Long current = insightDomainService.getRecordedInsightNumber(participation);

        return challengeAssembler.toParticipationProgressResponse(participation, current);
    }
}
