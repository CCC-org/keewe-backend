package ccc.keewedomain.persistence.repository.insight;

import ccc.keewedomain.domain.insight.ReactionAggregation;
import ccc.keewedomain.persistence.domain.insight.id.ReactionAggregationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface ReactionAggregationRepository extends JpaRepository<ReactionAggregation, ReactionAggregationId> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ra from ReactionAggregation ra where ra.id = :id")
    Optional<ReactionAggregation> findByIdWithReadWriteLock(@Param("id") ReactionAggregationId id);
}
