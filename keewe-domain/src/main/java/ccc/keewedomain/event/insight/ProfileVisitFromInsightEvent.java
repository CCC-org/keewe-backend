package ccc.keewedomain.event.insight;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(staticName = "of")
@Getter
public class ProfileVisitFromInsightEvent {
    private Long userId;
    private Long insightId;
}
