package ccc.keewedomain.domain.nest.id;

import ccc.keewedomain.domain.nest.Post;
import ccc.keewedomain.domain.user.Profile;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@EqualsAndHashCode
public class PostLikeId implements Serializable {
    private Profile profile;
    private Post post;
}
