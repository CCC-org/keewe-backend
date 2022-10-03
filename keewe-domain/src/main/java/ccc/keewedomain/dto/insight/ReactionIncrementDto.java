package ccc.keewedomain.dto.insight;

import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReactionIncrementDto {
    private Long insightId;
    private ReactionType reactionType;
    private Long value;

    public static ReactionIncrementDto of(Long insightId, ReactionType reactionType, Long value) {
        ReactionIncrementDto dto = new ReactionIncrementDto();
        dto.insightId = insightId;
        dto.reactionType = reactionType;
        dto.value = value;

        return dto;
    }
}
