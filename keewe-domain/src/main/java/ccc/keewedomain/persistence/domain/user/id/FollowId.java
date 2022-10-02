package ccc.keewedomain.persistence.domain.user.id;

import ccc.keewedomain.persistence.domain.user.User;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class FollowId implements Serializable {
    private User follower;
    private User followee;
}