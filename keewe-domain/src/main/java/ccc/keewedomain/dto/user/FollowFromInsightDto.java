package ccc.keewedomain.dto.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor(staticName = "of")
@Getter
@EqualsAndHashCode
public class FollowFromInsightDto implements Serializable {
    private final Long followerId;
    private final Long followeeId;
    private final Long insightId;
}
