package ccc.keewedomain.persistence.domain.challenge;

import ccc.keewedomain.persistence.domain.user.*;
import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "challenge_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChallengeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_record_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_participation_id")
    private ChallengeParticipation challengeParticipation;

    @Column(name = "week")
    private int weekCount;

    @Column(name = "record_count")
    private int recordCount;

    @Column(name = "goal_count")
    private int goalCount;

    @Column(name = "success")
    private boolean success;

    public static ChallengeRecord initialize(User user, ChallengeParticipation challengeParticipation, int weekCount, int recordCount, int goalCount) {
        ChallengeRecord challengeRecord = new ChallengeRecord();
        challengeRecord.user = user;
        challengeRecord.challengeParticipation = challengeParticipation;
        challengeRecord.weekCount = weekCount;
        challengeRecord.recordCount = recordCount;
        challengeRecord.goalCount = goalCount;
        challengeRecord.success = false;
        return challengeRecord;
    }
}
