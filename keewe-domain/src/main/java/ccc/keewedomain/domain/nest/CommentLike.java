package ccc.keewedomain.domain.nest;

import ccc.keewedomain.domain.user.Profile;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@IdClass(CommentLikeId.class)
public class CommentLike {

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
