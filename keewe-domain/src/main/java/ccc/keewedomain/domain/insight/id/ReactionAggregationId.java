package ccc.keewedomain.domain.insight.id;

import ccc.keewedomain.domain.insight.Insight;
import ccc.keewedomain.domain.insight.enums.ReactionType;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class ReactionAggregationId implements Serializable {
    private Insight insight;
    private ReactionType type;
}
