package ccc.keewedomain.dto.insight;

import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class ReactionIncrementDto implements Serializable {
    private Long insightId;
    private Long userId;
    private ReactionType reactionType;
    private Long value;

    public static ReactionIncrementDto of(Long insightId, Long userId, ReactionType reactionType, Long value) {
        ReactionIncrementDto dto = new ReactionIncrementDto();
        dto.insightId = insightId;
        dto.userId = userId;
        dto.reactionType = reactionType;
        dto.value = value;

        return dto;
    }
}
