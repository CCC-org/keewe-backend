package ccc.keewedomain.domain.insight;

import ccc.keewedomain.domain.common.BaseTimeEntity;
import ccc.keewedomain.domain.insight.enums.ReactionType;
import ccc.keewedomain.domain.insight.id.ReactionId;
import ccc.keewedomain.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import static javax.persistence.FetchType.LAZY;

@Getter
@Table(name = "reaction")
@Entity
@IdClass(ReactionId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static Reaction of(Insight insight, User reactor, ReactionType type) {
        Reaction entity = new Reaction();
        entity.insight = insight;
        entity.reactor = reactor;
        entity.type = type;
        return entity;
    }
}
