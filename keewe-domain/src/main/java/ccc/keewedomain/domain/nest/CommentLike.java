package ccc.keewedomain.domain.nest;

import ccc.keewedomain.domain.common.BaseTimeEntity;
import ccc.keewedomain.domain.nest.id.CommentLikeId;
import ccc.keewedomain.domain.user.Profile;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "comment_like")
@IdClass(CommentLikeId.class)
public class CommentLike extends BaseTimeEntity {

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;
}
