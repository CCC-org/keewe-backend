package ccc.keewedomain.event.insight;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
public class ProfileVisitFromInsightEvent implements Serializable {
    private Long insightId;
    private Long userId;
}
