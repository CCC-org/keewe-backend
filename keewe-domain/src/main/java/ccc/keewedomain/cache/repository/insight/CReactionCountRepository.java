package ccc.keewedomain.cache.repository.insight;

import ccc.keewedomain.cache.domain.insight.CReactionCount;
import ccc.keewedomain.dto.insight.ReactionAggregationGetDto;
import org.springframework.data.repository.CrudRepository;

import java.util.function.Supplier;

public interface CReactionCountRepository extends CrudRepository<CReactionCount, Long> {
    org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CReactionCountRepository.class);

    default CReactionCount findByIdWithMissHandle(Long id, Supplier<ReactionAggregationGetDto> supplier) {
        return findById(id).orElseGet(() -> {
            log.info("[CRCR::findByIdWithMissHandle] cache miss. insightId={}", id);

            ReactionAggregationGetDto dto = supplier.get();

            CReactionCount cReactionCount = CReactionCount.of(
                    id,
                    dto.getClap(),
                    dto.getHeart(),
                    dto.getSad(),
                    dto.getSurprise(),
                    dto.getFire(),
                    dto.getEyes()
            );
            save(cReactionCount);

            return cReactionCount;
        });
    }
}
