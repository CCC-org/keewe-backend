package ccc.keewedomain.domain.user;

import ccc.keewedomain.domain.common.BaseTimeEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Follow extends BaseTimeEntity {
    @Id
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "followee_id")
    private User followee;
}
