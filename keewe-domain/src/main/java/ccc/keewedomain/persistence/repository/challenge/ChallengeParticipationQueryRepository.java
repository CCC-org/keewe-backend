package ccc.keewedomain.persistence.repository.challenge;

import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.challenge.enums.ChallengeParticipationStatus;

import java.util.Optional;

public interface ChallengeParticipationQueryRepository {

    boolean existsByChallengerIdAndStatus(Long challengerId, ChallengeParticipationStatus status);
    Optional<ChallengeParticipation> findByChallengerIdAndStatusWithChallenge(Long challengerId, ChallengeParticipationStatus status);
}
