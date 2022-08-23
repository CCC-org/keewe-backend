package ccc.keewedomain.domain.challenge;

import ccc.keewedomain.domain.common.BaseTimeEntity;
import ccc.keewedomain.domain.user.User;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "challenge_participation")
@Getter
public class ChallengeParticipation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_participation_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "challenger_id", nullable = false)
    private User challenger;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @Column(name = "my_topic", nullable = false, length = 25)
    private String myTopic;

    @Column(name = "insight_per_week", nullable = false)
    private int insightPerWeek;

    @Column(name = "duration", nullable = false)
    private int duration; // 주 단위

    @Column(name = "deleted", nullable = false)
    private boolean deleted;
}
