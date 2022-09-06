package ccc.keewedomain.domain.user;

import ccc.keewedomain.domain.common.BaseTimeEntity;
import ccc.keewedomain.domain.user.id.FollowId;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Table(name = "follow")
@Entity
@IdClass(FollowId.class)
public class Follow extends BaseTimeEntity {

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "followee_id")
    private User followee;
}
