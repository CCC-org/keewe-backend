package ccc.keewedomain.cache.domain.insight.id;

import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@AllArgsConstructor
@EqualsAndHashCode
public class CReactionCountId implements Serializable {
    private Long insightId;
    private ReactionType reactionType;
}
