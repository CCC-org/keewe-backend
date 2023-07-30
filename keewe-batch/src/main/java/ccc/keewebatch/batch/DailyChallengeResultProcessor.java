package ccc.keewebatch.batch;

import ccc.keewebatch.helper.UniqueRunIdIncrementer;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import ccc.keewecore.utils.DateTimeUtils;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.challenge.ChallengeRecord;
import ccc.keewedomain.persistence.domain.notification.Notification;
import ccc.keewedomain.persistence.domain.notification.enums.NotificationContents;
import ccc.keewedomain.persistence.repository.challenge.ChallengeRecordRepository;
import ccc.keewedomain.service.notification.command.NotificationCommandDomainService;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Slf4j
@Configuration
public class DailyChallengeResultProcessor {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final ChallengeRecordRepository challengeRecordRepository;
    private final NotificationCommandDomainService notificationCommandDomainService;

    private final int CHUCK_SIZE = 100;

    @Bean("dailyChallengeResultProcessJob")
    public Job dailyChallengeResultProcessJob(Step dailyChallengeResultProcessStep) {
        return jobBuilderFactory.get("dailyChallengeResultProcessJob")
                .preventRestart()
                .incrementer(new UniqueRunIdIncrementer())
                .start(dailyChallengeResultProcessStep)
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        log.info("[DailyChallengeResultProcessor] 데일리 챌린지 성공/실패 처리 시작");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        log.info("[DailyChallengeResultProcessor] 데일리 챌린지 성공/실패 처리 종료");
                    }
                })
                .build();
    }

    @Bean("dailyChallengeResultProcessStep")
    public Step dailyChallengeResultProcessStep() {
        return stepBuilderFactory.get("dailyChallengeResultProcessStep")
                .<ChallengeParticipation, ChallengeParticipation>chunk(CHUCK_SIZE)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean("dailyChallengeResultProcessReader")
    public JpaCursorItemReader<ChallengeParticipation> reader() {
        return new JpaCursorItemReaderBuilder<ChallengeParticipation>()
                .name("dailyChallengeResultProcessReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(
                        "SELECT cp "
                                + "FROM ChallengeParticipation cp "
                                + "WHERE cp.status = 'CHALLENGING'")
                .build();
    }

    @Bean("dailyChallengeResultProcessWriter")
    public ItemWriter<ChallengeParticipation> writer() {
        return new JpaItemWriterBuilder<ChallengeParticipation>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean("dailyChallengeResultProcessProcessor")
    public ItemProcessor<ChallengeParticipation, ChallengeParticipation> processor() {
        return cp -> {
            boolean isChallengeEnded = cp.getEndDate().isBefore(ChronoLocalDate.from(LocalDateTime.now()));
            if (isChallengeEnded) {
                return processChallengeResult(cp);
            }
            this.notifyConditionally(cp);
            return cp;
        };
    }

    private ChallengeParticipation processChallengeResult(ChallengeParticipation cp) {
        List<ChallengeRecord> challengeRecords = challengeRecordRepository.findByChallengeParticipation(cp);
        boolean isFail = challengeRecords.stream()
                .anyMatch(record -> {
                    int recordCount = record.getRecordCount();
                    int goalCount = record.getGoalCount();
                    // note. 목표 치 보다 기록 적은 경우
                    return recordCount < goalCount;
                });
        if (isFail) {
            log.info("[DailyChallengeResultProcessor] 챌린지 실패 처리 - userId({}), cpId({})", cp.getChallenger().getId(), cp.getId());
            Notification notification = Notification.of(cp.getChallenger(), NotificationContents.챌린지_실패, cp.getChallenge().getId().toString());
            notificationCommandDomainService.save(notification);
            cp.expire();
        } else {
            log.info("[DailyChallengeResultProcessor] 챌린지 성공 처리 - userId({}), cpId({})", cp.getChallenger().getId(), cp.getId());
            Notification notification = Notification.of(cp.getChallenger(), NotificationContents.챌린지_성공, cp.getChallenge().getId().toString());
            notificationCommandDomainService.save(notification);
            cp.complete();
        }
        return cp;
    }

    // note. 챌린지 기록 수에 따라 참여 권유를 위한 알림 발송
    private void notifyConditionally(ChallengeParticipation cp) {
        int currentWeek = DateTimeUtils.getWeekCountByRange(cp.getCreatedAt(), cp.getEndDate().atTime(LocalTime.MIDNIGHT), LocalDateTime.now());
        ChallengeRecord challengeRecord = challengeRecordRepository.findByChallengeParticipationAndWeekCount(cp, currentWeek)
                .orElseThrow(() -> {
                    log.error("[DailyChallengeResultProcessor] 챌린지 기록 없음 - currentWeek({}), challengeParticipationId({})", currentWeek, cp.getId());
                    return new KeeweException(KeeweRtnConsts.ERR999);
                });

        int remainWeekDays = getRemainDays(cp.getCreatedAt(), LocalDateTime.now());
        // note. 일주일의 남은 기록 수와 남은 일 수가 같은 경우
        if (remainWeekDays == (challengeRecord.getGoalCount() - challengeRecord.getRecordCount())) {
            log.info("[DailyChallengeResultProcessor] 챌린지 채찍 알림 생성 - userId({}), cpId({})", cp.getChallenger().getId(), cp.getId());
            Notification notification = Notification.of(cp.getChallenger(), NotificationContents.챌린지_채찍, cp.getChallenge().getId().toString());
            notificationCommandDomainService.save(notification);
        }
    }

    private int getRemainDays(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return (7 - Math.abs(startDateTime.getDayOfWeek().getValue() - endDateTime.getDayOfWeek().getValue())) % 7;
    }
}
