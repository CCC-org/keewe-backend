package ccc.keeweapi.dto.insight;

import ccc.keeweapi.validator.annotations.Enum;
import ccc.keewedomain.domain.insight.enums.ReactionType;
import lombok.Getter;

@Getter
public class ReactionRequest {
    private Long insightId;

    @Enum(enumClass = ReactionType.class)
    private ReactionType reactionType;
}
