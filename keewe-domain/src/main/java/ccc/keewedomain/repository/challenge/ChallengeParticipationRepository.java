package ccc.keewedomain.repository.challenge;

import ccc.keewedomain.domain.challenge.Challenge;
import ccc.keewedomain.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.domain.challenge.enums.ChallengeParticipationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChallengeParticipationRepository extends JpaRepository<ChallengeParticipation, Long> {
    //TODO querydsl 적용 필요
    @Query("select (count(c) > 0) from ChallengeParticipation c " +
            "where c.challenger.id = ?1 and c.status = ?2")
    boolean existsByChallengerIdAndStatus(Long challengerId, ChallengeParticipationStatus status);

    Optional<ChallengeParticipation> findByChallengerIdAndStatus(Long userId, ChallengeParticipationStatus status);
}
