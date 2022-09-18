package ccc.keewedomain.domain.insight.id;

import ccc.keewedomain.domain.insight.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReactionId implements Serializable {
    private Long insight;
    private Long reactor;
    private ReactionType type;
}