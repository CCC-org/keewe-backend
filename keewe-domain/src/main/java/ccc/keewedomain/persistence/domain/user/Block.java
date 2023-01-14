package ccc.keewedomain.persistence.domain.user;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import ccc.keewedomain.persistence.domain.user.id.BlockId;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "block")
@IdClass(BlockId.class)
@Getter
public class Block extends BaseTimeEntity {

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "blocked_user_id")
    private User blockedUser;
}
