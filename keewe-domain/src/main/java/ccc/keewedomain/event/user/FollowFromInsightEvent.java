package ccc.keewedomain.event.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor(staticName = "of")
@EqualsAndHashCode
public class FollowFromInsightEvent implements Serializable {
    private Long followerId;
    private Long followeeId;
    private Long insightId;
}
