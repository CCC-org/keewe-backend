package ccc.keewedomain.dto.insight;

import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReactionDto {
    private Long insightId;
    private ReactionType reactionType;
    private Long count;

    public static ReactionDto of(Long insightId, ReactionType reactionType, Long count) {
        ReactionDto dto = new ReactionDto();
        dto.insightId = insightId;
        dto.reactionType = reactionType;
        dto.count = count;

        return dto;
    }
}
