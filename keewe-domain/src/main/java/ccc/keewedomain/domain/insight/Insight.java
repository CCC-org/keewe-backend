package ccc.keewedomain.domain.insight;

import ccc.keewedomain.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.domain.common.BaseTimeEntity;
import ccc.keewedomain.domain.common.Link;
import ccc.keewedomain.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "insight")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Insight extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "insight_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "challenge_participation_id")
    private ChallengeParticipation challengeParticipation;

    @Column(name = "contents", nullable = false, length = 300)
    private String contents;

    @Embedded
    private Link link;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "insight", fetch = LAZY, orphanRemoval = true)
    private List<Reaction> reactions = new ArrayList<>();

    @OneToMany(mappedBy = "insight", fetch = LAZY, orphanRemoval = true)
    private List<ReactionAggregation> reactionAggregations = new ArrayList<>();

    @OneToMany(mappedBy = "insight", fetch = LAZY)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "drawer_id")
    private Drawer drawer;

    public static Insight of(User writer, ChallengeParticipation challengeParticipation, Drawer drawer, String contents, Link link) {
        Insight insight = new Insight();
        insight.writer = writer;
        insight.challengeParticipation = challengeParticipation;
        insight.drawer = drawer;
        insight.contents = contents;
        insight.link = link;

        return insight;
    }

}
