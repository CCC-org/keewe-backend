package ccc.keewedomain.service.challenge;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.domain.challenge.Challenge;
import ccc.keewedomain.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.domain.user.User;
import ccc.keewedomain.dto.challenge.ChallengeCreateDto;
import ccc.keewedomain.dto.challenge.ChallengeParticipateDto;
import ccc.keewedomain.repository.challenge.ChallengeParticipationRepository;
import ccc.keewedomain.repository.challenge.ChallengeRepository;
import ccc.keewedomain.service.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static ccc.keewedomain.domain.challenge.enums.ChallengeParticipationStatus.CHALLENGING;

@Service
@RequiredArgsConstructor
public class ChallengeDomainService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeParticipationRepository challengeParticipationRepository;
    private final UserDomainService userDomainService;

    public Challenge save(ChallengeCreateDto dto) {
        User writer = userDomainService.getUserByIdOrElseThrow(dto.getUserId());
        Challenge challenge = Challenge.of(writer, dto.getName(), dto.getInterest(), dto.getIntroduction());
        return challengeRepository.save(challenge);
    }

    public ChallengeParticipation participate(ChallengeParticipateDto dto) {
        User challenger = userDomainService.getUserByIdOrElseThrow(dto.getChallengerId());
        validateNotOnChallenge(dto.getChallengerId());
        Challenge challenge = getByIdOrElseThrow(dto.getChallengeId());
        return challenge.participate(challenger, dto.getMyTopic(), dto.getInsightPerWeek(), dto.getDuration());
    }

    public boolean onChallenge(Long userId) {
        return challengeParticipationRepository.existsByChallengerIdAndStatus(userId, CHALLENGING);
    }

    public Challenge getByIdOrElseThrow(Long id) {
        return challengeRepository.findById(id).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR430));
    }

    private void validateNotOnChallenge(Long userId) {
        if (onChallenge(userId)) {
            throw new KeeweException(KeeweRtnConsts.ERR431);
        }
    }
}
