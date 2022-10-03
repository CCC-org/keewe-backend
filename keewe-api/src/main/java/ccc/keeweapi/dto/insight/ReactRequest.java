package ccc.keeweapi.dto.insight;

import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReactRequest {
    private Long insightId;
    private ReactionType reactionType;
    private Long value;
}
