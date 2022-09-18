package ccc.keewedomain.repository.challenge;

import ccc.keewedomain.domain.challenge.enums.ChallengeParticipationStatus;

public interface ChallengeParticipationQueryRepository {

    boolean existsByChallengerIdAndStatus(Long challengerId, ChallengeParticipationStatus status);
}
