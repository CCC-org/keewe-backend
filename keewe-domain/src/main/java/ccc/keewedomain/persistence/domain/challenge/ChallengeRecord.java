package ccc.keewedomain.persistence.domain.challenge;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "challenge_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChallengeRecord extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_record_id")
    private Long id;

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

    public static ChallengeRecord initialize(ChallengeParticipation challengeParticipation, int weekCount) {
        ChallengeRecord challengeRecord = new ChallengeRecord();
        challengeRecord.challengeParticipation = challengeParticipation;
        challengeRecord.weekCount = weekCount;
        challengeRecord.recordCount = 0;
        challengeRecord.goalCount = challengeParticipation.getInsightPerWeek();
        challengeRecord.success = false;
        return challengeRecord;
    }

    // TODO(YHS). 한 개 씩 증가하는 방식으로 업데이트
    public void updateRecordCount(int recordCount) {
        this.recordCount = recordCount;
        if (recordCount >= goalCount) {
            this.success = true;
        }
    }
}
