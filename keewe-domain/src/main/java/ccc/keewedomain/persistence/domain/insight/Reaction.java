package ccc.keewedomain.persistence.domain.insight;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import ccc.keewedomain.persistence.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static javax.persistence.FetchType.LAZY;

@Table(name = "reaction")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reaction extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "insight_id")
    private Insight insight;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reactor_id")
    private User reactor;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type")
    private ReactionType type;

    public static Reaction of(Insight insight, User reactor, ReactionType reactionType) {
        Reaction entity = new Reaction();
        entity.insight = insight;
        entity.reactor = reactor;
        entity.type = reactionType;

        return entity;
    }

    public Long getId() {
        return id;
    }

    public Insight getInsight() {
        return insight;
    }

    public User getReactor() {
        return reactor;
    }

    public ReactionType getType() {
        return type;
    }
}
