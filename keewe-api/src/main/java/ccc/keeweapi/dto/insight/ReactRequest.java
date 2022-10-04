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

    public static ReactRequest of(Long insightId, ReactionType reactionType, Long value) {
        ReactRequest request = new ReactRequest();
        request.insightId = insightId;
        request.reactionType = reactionType;
        request.value = value;

        return request;
    }
}
