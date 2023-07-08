package ccc.keewedomain.persistence.domain.user.id;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FollowId implements Serializable {
    private Long follower;
    private Long followee;
}
