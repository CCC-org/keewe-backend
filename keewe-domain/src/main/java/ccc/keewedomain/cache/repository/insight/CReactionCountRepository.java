package ccc.keewedomain.cache.repository.insight;

import ccc.keewedomain.cache.domain.insight.CReactionCount;
import ccc.keewedomain.cache.domain.insight.id.CReactionCountId;
import ccc.keewedomain.domain.insight.ReactionAggregation;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import org.springframework.data.repository.CrudRepository;

import java.util.function.Supplier;

public interface CReactionCountRepository extends CrudRepository<CReactionCount, String> {
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CReactionCountRepository.class);

    default CReactionCount findByIdWithMissHandle(CReactionCountId id, Supplier<ReactionAggregation> supplier) {
        return findById(id.toString()).orElseGet(() -> {
            Long insightId = id.getInsightId();
            ReactionType reactionType = id.getReactionType();
            log.info("[CRCR::findByIdWithMissHandle] cache miss. insightId={}, reactionType={}", insightId, reactionType);

            ReactionAggregation reactionAggregation = supplier.get();

            CReactionCount cReactionCount = CReactionCount.of(id.toString(), reactionAggregation.getCount());
            save(cReactionCount);

            return cReactionCount;
        });
    }
}
