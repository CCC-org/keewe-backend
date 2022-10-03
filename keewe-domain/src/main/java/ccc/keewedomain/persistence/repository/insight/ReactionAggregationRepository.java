package ccc.keewedomain.persistence.repository.insight;

import ccc.keewedomain.domain.insight.ReactionAggregation;
import ccc.keewedomain.persistence.domain.insight.id.ReactionAggregationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionAggregationRepository extends JpaRepository<ReactionAggregation, ReactionAggregationId> {
}
