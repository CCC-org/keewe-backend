package ccc.keeweapi.dto.insight.response;

import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReactResponse {
    private Long insightId;
    private ReactionType reactionType;
    private Long count;

    public static ReactResponse of(Long insightId, ReactionType reactionType, Long count) {
        ReactResponse response = new ReactResponse();
        response.insightId = insightId;
        response.reactionType = reactionType;
        response.count = count;

        return response;
    }
}
