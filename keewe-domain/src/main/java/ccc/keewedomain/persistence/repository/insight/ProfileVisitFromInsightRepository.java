package ccc.keewedomain.persistence.repository.insight;

import ccc.keewedomain.persistence.domain.insight.ProfileVisitFromInsight;
import ccc.keewedomain.persistence.domain.insight.id.ProfileVisitFromInsightId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileVisitFromInsightRepository extends JpaRepository<ProfileVisitFromInsight, ProfileVisitFromInsightId> {
}
