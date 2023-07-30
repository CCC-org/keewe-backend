package ccc.keewedomain.persistence.domain.challenge;

import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewedomain.persistence.domain.challenge.enums.ChallengeParticipationStatus;
import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import ccc.keewedomain.persistence.domain.user.User;
import java.time.LocalDate;
import java.time.Period;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import static javax.persistence.FetchType.LAZY;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import static lombok.AccessLevel.PROTECTED;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "challenge_participation")
@NoArgsConstructor(access = PROTECTED)
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

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChallengeParticipationStatus status;

    public static ChallengeParticipation of(User challenger, Challenge challenge, String myTopic, int insightPerWeek, int duration) {
        ChallengeParticipation participation = new ChallengeParticipation();
        participation.challenger = challenger;
        participation.challenge = challenge;
        participation.myTopic = myTopic;
        participation.insightPerWeek = insightPerWeek;
        participation.duration = duration;
        participation.status = ChallengeParticipationStatus.CHALLENGING;
        participation.initEndDate();

        return participation;
    }

    // 현재가 몇 주차인지
    public long getCurrentWeek() {
        LocalDate createdAt = getCreatedAt().toLocalDate();
        LocalDate today = LocalDate.now();
        Period between = Period.between(createdAt, today);

        return between.getDays() / 7 + 1; // 1주차부터 시작
    }

    public LocalDate getStartDateOfThisWeek() {
        return getCreatedAt().plusWeeks(getCurrentWeek() - 1).toLocalDate();
    }

    public Long getTotalInsightNumber() {
        return (long) (insightPerWeek * duration);
    }

    public void update(String myTopic, int insightPerWeek, int duration) {
        this.myTopic = myTopic;
        this.insightPerWeek = insightPerWeek;
        this.duration = duration;
        initEndDate();
    }

    public void expire() {
        this.status = ChallengeParticipationStatus.EXPIRED;
    }

    public void cancel() {
        this.endDate = LocalDate.now();
        this.status = ChallengeParticipationStatus.CANCELED;
    }

    public void complete() {
        this.status = ChallengeParticipationStatus.COMPLETED;
    }

    public boolean isChallenging() {
        return this.status == ChallengeParticipationStatus.CHALLENGING;
    }

    private void initEndDate() {
        LocalDate createdDate = getCreatedAt().toLocalDate();
        LocalDate newEndDate = createdDate.minusDays(1).plusWeeks(duration);
        validateEndDate(newEndDate);
        this.endDate = newEndDate;
    }

    private void validateEndDate(LocalDate newEndDate) {
        if(newEndDate.isBefore(LocalDate.now())) {
            throw new KeeweException(KeeweRtnConsts.ERR433);
        }
    }
}
