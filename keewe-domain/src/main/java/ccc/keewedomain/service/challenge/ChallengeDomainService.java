package ccc.keewedomain.service.challenge;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.dto.challenge.ChallengeCreateDto;
import ccc.keewedomain.dto.challenge.ChallengeParticipateDto;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.user.User;
import ccc.keewedomain.persistence.repository.challenge.ChallengeParticipationRepository;
import ccc.keewedomain.persistence.repository.challenge.ChallengeRepository;
import ccc.keewedomain.persistence.repository.insight.InsightQueryRepository;
import ccc.keewedomain.service.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static ccc.keewedomain.persistence.domain.challenge.enums.ChallengeParticipationStatus.CHALLENGING;

@Service
@RequiredArgsConstructor
public class ChallengeDomainService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeParticipationRepository challengeParticipationRepository;
    private final InsightQueryRepository insightQueryRepository;

    private final UserDomainService userDomainService;

    public Challenge save(ChallengeCreateDto dto) {
        User writer = userDomainService.getUserByIdOrElseThrow(dto.getUserId());
        Challenge challenge = Challenge.of(writer, dto.getName(), dto.getInterest(), dto.getIntroduction());
        return challengeRepository.save(challenge);
    }

    public ChallengeParticipation participate(ChallengeParticipateDto dto) {
        User challenger = userDomainService.getUserByIdOrElseThrow(dto.getChallengerId());
        exitCurrentChallengeIfExist(challenger);
        Challenge challenge = getByIdOrElseThrow(dto.getChallengeId());
        return challenge.participate(challenger, dto.getMyTopic(), dto.getInsightPerWeek(), dto.getDuration());
    }

    public Challenge getByIdOrElseThrow(Long id) {
        return challengeRepository.findById(id).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR430));
    }

    public boolean checkParticipation(Long userId) {
        return challengeParticipationRepository.existsByChallengerIdAndStatus(userId, CHALLENGING);
    }

    public ChallengeParticipation getCurrentChallengeParticipation(User challenger) {
        return findCurrentChallengeParticipation(challenger).orElseThrow(() -> new KeeweException(KeeweRtnConsts.ERR432));
    }

    public Optional<ChallengeParticipation> findCurrentParticipationWithChallenge(Long challengerId) {
        return challengeParticipationRepository.findByChallengerIdAndStatusWithChallenge(challengerId, CHALLENGING);
    }

    private void exitCurrentChallengeIfExist(User challenger) {
        findCurrentChallengeParticipation(challenger).ifPresent(ChallengeParticipation::cancel);
    }

    public Optional<ChallengeParticipation> findCurrentChallengeParticipation(User challenger) {
        return challengeParticipationRepository.findByChallengerAndStatusAndDeletedFalse(challenger, CHALLENGING);
    }
}
