package ccc.keewedomain.repository.challenge;

import ccc.keewedomain.domain.challenge.enums.ChallengeParticipationStatus;

public interface ChallengeParticipationRepositoryCustom {

    boolean existsByChallengerIdAndStatus(Long challengerId, ChallengeParticipationStatus status);
}
