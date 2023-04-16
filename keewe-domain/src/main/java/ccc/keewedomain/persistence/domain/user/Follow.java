package ccc.keewedomain.persistence.domain.user;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import ccc.keewedomain.persistence.domain.user.id.FollowId;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Table(name = "follow")
@Entity
@IdClass(FollowId.class)
@Getter
public class Follow extends BaseTimeEntity {

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "follower_id")
    private User follower; // note. 팔로우 하는 사람

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "followee_id")
    private User followee; // note. 팔로우 당하는 사람

    public static Follow makeRelation(User follower, User followee) {
        Follow follow = new Follow();
        follow.follower = follower;
        follow.followee = followee;

        follower.getFollowees().add(follow);
        followee.getFollowers().add(follow);

        return follow;
    }


    public void removeRelation(User follower, User followee) {
        follower.getFollowees().remove(this);
        followee.getFollowers().remove(this);
    }
}
