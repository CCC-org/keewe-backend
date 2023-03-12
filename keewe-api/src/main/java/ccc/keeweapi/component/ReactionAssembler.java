package ccc.keeweapi.component;

import ccc.keeweapi.dto.insight.ReactRequest;
import ccc.keeweapi.dto.insight.ReactResponse;
import ccc.keeweapi.dto.insight.ReactionAggregationResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.dto.insight.ReactionDto;
import ccc.keewedomain.dto.insight.ReactionIncrementDto;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ReactionAssembler {
    public ReactionIncrementDto toReactionIncrementDto(ReactRequest request) {
        return ReactionIncrementDto.of(
                request.getInsightId(),
                SecurityUtil.getUser().getId(),
                request.getReactionType(),
                request.getValue()
        );
    }

    public ReactResponse toReactResponse(ReactionDto dto) {
        return ReactResponse.of(dto.getInsightId(), dto.getReactionType(), dto.getCount());
    }

    public ReactionAggregationResponse toReactionAggregationResponse(Map<ReactionType, Long> reactionCntMap) {
        return ReactionAggregationResponse.of(
                reactionCntMap.getOrDefault(ReactionType.CLAP, 0L),
                reactionCntMap.getOrDefault(ReactionType.HEART, 0L),
                reactionCntMap.getOrDefault(ReactionType.SAD, 0L),
                reactionCntMap.getOrDefault(ReactionType.SURPRISE, 0L),
                reactionCntMap.getOrDefault(ReactionType.FIRE, 0L),
                reactionCntMap.getOrDefault(ReactionType.EYES, 0L)
        );
    }
}
