package ccc.keewedomain.persistence.repository.challenge;

import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.challenge.ChallengeRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord, Long> {
    Optional<ChallengeRecord> findByChallengeParticipationAndWeekCount(ChallengeParticipation challengeParticipation, int weekCount);
    List<ChallengeRecord> findByChallengeParticipation(ChallengeParticipation challengeParticipation);
}
