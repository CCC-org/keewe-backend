package ccc.keewedomain.domain.insight.id;

import ccc.keewedomain.domain.insight.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ReactionAggregationId implements Serializable {
    private Long insight;
    private ReactionType type;
}
