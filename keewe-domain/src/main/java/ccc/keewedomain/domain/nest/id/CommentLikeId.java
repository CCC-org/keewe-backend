package ccc.keewedomain.domain.nest.id;

import ccc.keewedomain.domain.nest.Comment;
import ccc.keewedomain.domain.user.Profile;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class CommentLikeId implements Serializable {
    private Comment comment;
    private Profile profile;
}
