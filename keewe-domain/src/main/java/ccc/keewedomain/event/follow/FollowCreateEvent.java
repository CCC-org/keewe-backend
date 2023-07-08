package ccc.keewedomain.event.follow;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor(staticName = "of")
public class FollowCreateEvent implements Serializable {
    private final Long followerId;
    private final Long followeeId;
    private final Long refInsightId;
}
