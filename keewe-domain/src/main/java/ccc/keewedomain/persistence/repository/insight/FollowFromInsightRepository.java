package ccc.keewedomain.persistence.repository.insight;

import ccc.keewedomain.persistence.domain.insight.FollowFromInsight;
import ccc.keewedomain.persistence.domain.insight.id.FollowFromInsightId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowFromInsightRepository extends JpaRepository<FollowFromInsight, FollowFromInsightId> {
}
