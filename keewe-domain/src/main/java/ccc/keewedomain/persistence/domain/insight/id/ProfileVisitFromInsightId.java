package ccc.keewedomain.persistence.domain.insight.id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@EqualsAndHashCode
public class ProfileVisitFromInsightId implements Serializable {
    private Long insight;
    private Long user;
}
