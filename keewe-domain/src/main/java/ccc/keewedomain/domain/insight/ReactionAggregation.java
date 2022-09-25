package ccc.keewedomain.domain.insight;

import ccc.keewedomain.domain.common.BaseTimeEntity;
import ccc.keewedomain.domain.insight.enums.ReactionType;
import ccc.keewedomain.domain.insight.id.ReactionAggregationId;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Table(name = "reaction_aggregation")
@Entity
@IdClass(ReactionAggregationId.class)
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
}
