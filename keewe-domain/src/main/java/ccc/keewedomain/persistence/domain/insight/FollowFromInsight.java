package ccc.keewedomain.persistence.domain.insight;

import ccc.keewedomain.persistence.domain.insight.id.FollowFromInsightId;
import ccc.keewedomain.persistence.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "follow_from_insight")
@IdClass(FollowFromInsightId.class)
@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class FollowFromInsight {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_id")
    private User followee;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insight_id")
    private Insight insight;
}
