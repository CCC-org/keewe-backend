package ccc.keewedomain.persistence.repository.challenge;

import ccc.keewedomain.persistence.domain.challenge.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord, Long> {
}
