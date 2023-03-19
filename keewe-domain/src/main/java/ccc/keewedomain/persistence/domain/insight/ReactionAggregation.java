package ccc.keewedomain.persistence.domain.insight;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import ccc.keewedomain.persistence.domain.insight.enums.ReactionType;
import ccc.keewedomain.persistence.domain.insight.id.ReactionAggregationId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
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

    public static ReactionAggregation of(Insight insight, ReactionType reactionType, Long count) {
        ReactionAggregation entity = new ReactionAggregation();
        entity.insight = insight;
        entity.type = reactionType;
        entity.count = count;

        return entity;
    }

    public void incrementCountByValue(Long value) {
        count += value;
    }
}
