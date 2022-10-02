package ccc.keewedomain.persistence.repository.challenge;

import ccc.keewedomain.persistence.domain.challenge.enums.ChallengeParticipationStatus;

public interface ChallengeParticipationQueryRepository {

    boolean existsByChallengerIdAndStatus(Long challengerId, ChallengeParticipationStatus status);
}
