package ccc.keewedomain.cache.repository.insight;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.cache.domain.insight.CReactionCount;
import ccc.keewedomain.cache.domain.insight.id.CReactionCountId;
import ccc.keewedomain.domain.insight.ReactionAggregation;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import ccc.keewedomain.persistence.domain.insight.id.ReactionAggregationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface CReactionCountRepository extends CrudRepository<CReactionCount, String> {
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CReactionCountRepository.class);

    default CReactionCount findByIdWithMissHandle(CReactionCountId id, JpaRepository<ReactionAggregation, ReactionAggregationId> repository) {
        return findById(id.toString()).orElseGet(() -> {
            Long insightId = id.getInsightId();
            ReactionType reactionType = id.getReactionType();
            log.info("[CRCR::findByIdWithMissHandle] cache miss. insightId={}, reactionType={}", insightId, reactionType);

            ReactionAggregationId reactionAggregationId = new ReactionAggregationId(insightId, reactionType);
            ReactionAggregation reactionAggregation = repository.findById(reactionAggregationId)
                    .orElseThrow(() -> {
                        throw new KeeweException(KeeweRtnConsts.ERR471);
                    });

            CReactionCount cReactionCount = CReactionCount.of(id.toString(), reactionAggregation.getCount());
            save(cReactionCount);

            return cReactionCount;
        });
    }
}
