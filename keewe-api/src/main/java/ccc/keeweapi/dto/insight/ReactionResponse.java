package ccc.keeweapi.dto.insight;

import ccc.keeweapi.validator.annotations.Enum;
import ccc.keewedomain.domain.insight.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ReactionResponse {
    @Enum(enumClass = ReactionType.class)
    private ReactionType reactionType;
}
