package ccc.keewedomain.domain.nest.id;

import ccc.keewedomain.domain.insight.Comment;
import ccc.keewedomain.domain.user.User;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class CommentLikeId implements Serializable {
    private Comment comment;
    private User user;
}
