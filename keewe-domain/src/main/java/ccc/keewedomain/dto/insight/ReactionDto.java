package ccc.keewedomain.dto.insight;

import ccc.keewedomain.domain.insight.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class ReactionDto {
    private Long insightId;
    private Long userId;
    private ReactionType reactionType;
}
