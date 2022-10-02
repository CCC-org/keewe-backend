package ccc.keewedomain.persistence.domain.nest.id;

import ccc.keewedomain.persistence.domain.insight.Comment;
import ccc.keewedomain.persistence.domain.user.User;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class CommentLikeId implements Serializable {
    private Comment comment;
    private User user;
}
