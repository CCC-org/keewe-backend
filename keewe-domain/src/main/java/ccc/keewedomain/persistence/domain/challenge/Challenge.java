package ccc.keewedomain.persistence.domain.challenge;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import ccc.keewedomain.persistence.domain.common.Interest;
import ccc.keewedomain.persistence.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "challenge")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Challenge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id")
    private User writer;

    @Embedded
    private Interest interest;

    @Column(name = "name", nullable = false, length = 25)
    private String name;

    @Column(name = "introduction", length = 150)
    private String introduction;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ChallengeParticipation> participationList = new ArrayList<>();

    public static Challenge of(User writer, String name, String interest, String introduction) {
        Challenge challenge = new Challenge();
        challenge.writer = writer;
        challenge.interest = Interest.of(interest);
        challenge.name = name;
        challenge.introduction = introduction;

        return challenge;
    }

    public ChallengeParticipation participate(User challenger, String myTopic, int insightPerWeek, int duration) {
        ChallengeParticipation participation = ChallengeParticipation.of(
                challenger,
                this,
                myTopic,
                insightPerWeek,
                duration);
        getParticipationList().add(participation);
        this.deleted = false;
        return participation;
    }

    public static Challenge delete(Challenge challenge) {
        challenge.deleted = true;
        return challenge;
    }

    public boolean isNoOneParticipated() {
        return participationList.isEmpty() || participationList.stream()
                .noneMatch(ChallengeParticipation::isChallenging);
    }
}
