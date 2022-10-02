package ccc.keewedomain.persistence.domain.nest.id;

import ccc.keewedomain.persistence.domain.nest.Post;
import ccc.keewedomain.persistence.domain.user.User;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@EqualsAndHashCode
public class PostLikeId implements Serializable {
    private User user;
    private Post post;
}
