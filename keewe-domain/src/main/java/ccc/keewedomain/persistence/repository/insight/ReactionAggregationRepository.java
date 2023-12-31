package ccc.keewedomain.persistence.repository.insight;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.persistence.domain.insight.ReactionAggregation;
import ccc.keewedomain.dto.insight.ReactionAggregationGetDto;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import ccc.keewedomain.persistence.domain.insight.id.ReactionAggregationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ccc.keewedomain.persistence.domain.insight.enums.ReactionType.*;
import static ccc.keewedomain.persistence.domain.insight.enums.ReactionType.EYES;

public interface ReactionAggregationRepository extends JpaRepository<ReactionAggregation, ReactionAggregationId> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ra from ReactionAggregation ra where ra.id = :id")
    Optional<ReactionAggregation> findByIdWithReadWriteLock(@Param("id") ReactionAggregationId id);

    List<ReactionAggregation> findAllByInsightId(Long insightId);

    default ReactionAggregation findByIdOrElseThrow(ReactionAggregationId id) {
        return findById(id).orElseThrow(() -> {
            throw new KeeweException(KeeweRtnConsts.ERR471);
        });
    }
    
    default ReactionAggregationGetDto findDtoByInsightId(Long insightId) {
        Map<ReactionType, Long> reactCntMap = findAllByInsightId(insightId).stream().collect(Collectors.toMap(ReactionAggregation::getType, ReactionAggregation::getCount));
        Arrays.stream(values()).forEach((reactionType) -> {
            if (reactCntMap.get(reactionType) == null)
                throw new KeeweException(KeeweRtnConsts.ERR471);
        });
        return ReactionAggregationGetDto.of(
                insightId,
                reactCntMap.get(CLAP),
                false,
                reactCntMap.get(HEART),
                false,
                reactCntMap.get(SAD),
                false,
                reactCntMap.get(SURPRISE),
                false,
                reactCntMap.get(FIRE),
                false,
                reactCntMap.get(EYES),
                false
        );
    }
}
