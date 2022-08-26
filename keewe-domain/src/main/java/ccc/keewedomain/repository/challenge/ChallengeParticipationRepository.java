package ccc.keewedomain.repository.challenge;

import ccc.keewedomain.domain.challenge.ChallengeParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface ChallengeParticipationRepository extends JpaRepository<ChallengeParticipation, Long> {
    //TODO querydsl 적용 필요
    @Query("select (count(c) > 0) from ChallengeParticipation c " +
            "where c.challenge.id = ?1 and c.endDate > ?2 and c.deleted = false")
    boolean existsByChallengeIdAndEndDateAfterAndDeletedFalse(Long challengerId, LocalDate date);
}
