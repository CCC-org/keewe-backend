package ccc.keeweapi.dto.insight;

import ccc.keeweapi.utils.SecurityUtil;
import ccc.keewedomain.domain.insight.enums.ReactionType;
import ccc.keewedomain.dto.insight.ReactionDto;
import org.springframework.stereotype.Component;

@Component
public class InsightAssembler {
    public ReactionDto toReactionDto(ReactionRequest request) {
        return ReactionDto.of(
                request.getInsightId(),
                SecurityUtil.getUserId(),
                request.getReactionType()
        );
    }

    public ReactionResponse toReactionResponse(ReactionType reactionType) {
        return ReactionResponse.of(reactionType);
    }
}
