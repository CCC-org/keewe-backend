package ccc.keewedomain.service.challenge.command;

import ccc.keewedomain.dto.challenge.ChallengeCreateDto;
import ccc.keewedomain.dto.challenge.ChallengeParticipateDto;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.challenge.ChallengeRepository;
import ccc.keewedomain.service.challenge.query.ChallengeParticipateQueryDomainService;
import ccc.keewedomain.service.challenge.query.ChallengeQueryDomainService;
import ccc.keewedomain.service.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChallengeCommandDomainService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeQueryDomainService challengeQueryDomainService;
    private final ChallengeParticipateQueryDomainService challengeParticipateQueryDomainService;
    private final UserDomainService userDomainService;

    public Challenge save(ChallengeCreateDto dto) {
        User writer = userDomainService.getUserByIdOrElseThrow(dto.getUserId());
        Challenge challenge = Challenge.of(writer, dto.getName(), dto.getInterest(), dto.getIntroduction());
        return challengeRepository.save(challenge);
    }

    public ChallengeParticipation participate(ChallengeParticipateDto dto) {
        User challenger = userDomainService.getUserByIdOrElseThrow(dto.getChallengerId());
        exitCurrentChallengeIfExist(challenger);
        Challenge challenge = challengeQueryDomainService.getByIdOrElseThrow(dto.getChallengeId());
        return challenge.participate(challenger, dto.getMyTopic(), dto.getInsightPerWeek(), dto.getDuration());
    }

    private void exitCurrentChallengeIfExist(User challenger) {
        challengeParticipateQueryDomainService.findCurrentChallengeParticipation(challenger).ifPresent(ChallengeParticipation::cancel);
    }
}
