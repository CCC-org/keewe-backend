package ccc.keewedomain.persistence.repository.challenge;

import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.challenge.enums.ChallengeParticipationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChallengeParticipationRepository extends
        JpaRepository<ChallengeParticipation, Long>,
        ChallengeParticipationQueryRepository {
    //FIXME  userId 대신 엔티티로 조회하도록 변경하기
    Optional<ChallengeParticipation> findByChallengerIdAndStatus(Long userId, ChallengeParticipationStatus status);
}
