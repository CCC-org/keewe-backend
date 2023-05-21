package ccc.keewedomain.persistence.domain.user.id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowFromInsightId implements Serializable {
    private Long follower;
    private Long followee;
    private Long insight;
}