package ccc.keeweapi.service.challenge;

import ccc.keeweapi.dto.challenge.ChallengeAssembler;
import ccc.keeweapi.dto.challenge.ChallengeCreateRequest;
import ccc.keeweapi.dto.challenge.ChallengeCreateResponse;
import ccc.keewedomain.domain.challenge.Challenge;
import ccc.keewedomain.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.service.challenge.ChallengeDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ChallengeApiService {

    private final ChallengeDomainService challengeDomainService;
    private final ChallengeAssembler challengeAssembler;

    @Transactional
    public ChallengeCreateResponse createChallenge(ChallengeCreateRequest request) {
        Challenge challenge = challengeDomainService.save(challengeAssembler.toChallengeCreateDto(request));
        ChallengeParticipation participation = challengeDomainService
                .participate(challengeAssembler.toChallengeParticipateDto(request.getParticipation(), challenge.getId()));
        return challengeAssembler.toChallengeCreateResponse(challenge, participation);
    }
}
