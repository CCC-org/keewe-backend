package ccc.keewedomain.domain.nest;

import ccc.keewedomain.domain.user.Profile;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@EqualsAndHashCode
public class PostLikeId implements Serializable {
    private Profile profile;
    private Post post;
}
