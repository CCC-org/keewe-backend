package ccc.keewedomain.user;

import ccc.keewedomain.common.BaseTimeEntity;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Buddy extends BaseTimeEntity {
    @Id
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "follower_id")
    private Profile follower;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "followee_id")
    private Profile followee;
}
