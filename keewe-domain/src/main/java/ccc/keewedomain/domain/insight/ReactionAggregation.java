package ccc.keewedomain.domain.insight;

import ccc.keewedomain.domain.common.BaseTimeEntity;
import ccc.keewedomain.domain.insight.enums.ReactionType;
import ccc.keewedomain.domain.insight.id.ReactionAggregationId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Table(name = "reaction_aggregation")
@Entity
@IdClass(ReactionAggregationId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReactionAggregation extends BaseTimeEntity {
    @Id
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "insight_id")
    private Insight insight;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type")
    private ReactionType type;

    @Column(name = "count")
    private Long count;

    public void incrementCountBy(Long value) {
        count += value;
    }

    public void decrementCountBy(Long value) {
        count -= value;
    }

    public static ReactionAggregation of(Insight insight, ReactionType reactionType, Long count) {
        ReactionAggregation entity = new ReactionAggregation();
        entity.insight = insight;
        entity.type = reactionType;
        entity.count = count;
        return entity;
    }
}
