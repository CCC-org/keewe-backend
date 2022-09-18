package ccc.keewedomain.repository.insight;

import ccc.keewedomain.domain.insight.ReactionAggregation;
import ccc.keewedomain.domain.insight.id.ReactionAggregationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionAggregationRepository extends JpaRepository<ReactionAggregation, ReactionAggregationId> {
}
