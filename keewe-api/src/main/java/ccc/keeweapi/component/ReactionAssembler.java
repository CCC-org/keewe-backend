package ccc.keeweapi.component;

import ccc.keeweapi.dto.insight.ReactRequest;
import ccc.keeweapi.dto.insight.ReactResponse;
import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.dto.insight.ReactionDto;
import ccc.keewedomain.dto.insight.ReactionIncrementDto;
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
}
