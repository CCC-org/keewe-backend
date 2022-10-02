package ccc.keewedomain.persistence.domain.insight;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import ccc.keewedomain.persistence.domain.insight.id.ReactionId;
import ccc.keewedomain.persistence.domain.user.User;

import javax.persistence.*;
import static javax.persistence.FetchType.LAZY;

@Table(name = "reaction")
@Entity
@IdClass(ReactionId.class)
public class Reaction extends BaseTimeEntity {

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "insight_id")
    private Insight insight;

    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reactor_id")
    private User reactor;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type")
    private ReactionType type;
}
