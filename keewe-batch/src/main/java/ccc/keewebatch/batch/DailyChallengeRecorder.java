package ccc.keewebatch.batch;

import ccc.keewebatch.helper.UniqueRunIdIncrementer;
import ccc.keewecore.utils.DateTimeUtils;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.domain.challenge.ChallengeRecord;
import ccc.keewedomain.persistence.repository.challenge.ChallengeRecordRepository;
import ccc.keewedomain.service.insight.query.InsightQueryDomainService;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DailyChallengeRecorder {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final InsightQueryDomainService insightQueryDomainService;
    private final ChallengeRecordRepository challengeRecordRepository;

    private final int CHUCK_SIZE = 100;

    @Bean
    public Job challengeRecordCheckJob(Step challengeRecordStep) {
        return jobBuilderFactory.get("challengeRecordCheck")
                .preventRestart()
                .incrementer(new UniqueRunIdIncrementer())
                .start(challengeRecordStep)
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        log.info("[DailyChallengeRecorder] 데일리 챌린지 기록 시작");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        log.info("[DailyChallengeRecorder] 데일리 챌린지 기록 종료");
                    }
                })
                .build();
    }

    @Bean
    public Step challengeRecordStep() {
        return stepBuilderFactory.get("challengeRecordStep")
                .<ChallengeParticipation, ChallengeParticipation>chunk(CHUCK_SIZE)
                .reader(reader(null))
                .processor(processor(null))
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public JpaCursorItemReader<ChallengeParticipation> reader(@Value("#{jobParameters[endDateStr]}") String endDateStr) {
        LocalDate endDate = LocalDate.parse(endDateStr);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.MIN);
        Integer weekday = endDate.minusDays(1).getDayOfWeek().ordinal();

        Map<String, Object> params = new HashMap<>();
        params.put("weekday", weekday);
        params.put("endDate", endDateTime.minusDays(1L));

        return new JpaCursorItemReaderBuilder<ChallengeParticipation>()
                .name("sampleReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(
                        "SELECT cp "
                        + "FROM ChallengeParticipation cp "
                        + "WHERE cp.status = 'CHALLENGING'")
                .build();
    }

    @Bean
    public ItemWriter<ChallengeParticipation> writer() {
        return new JpaItemWriterBuilder<ChallengeParticipation>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<ChallengeParticipation, ChallengeParticipation> processor(@Value("#{jobParameters[endDateStr]}") String currentDateStr) {
        LocalDate currentDate = LocalDate.parse(currentDateStr);
        LocalDateTime currentDateTime = LocalDateTime.of(currentDate, LocalTime.MIN);
        // TODO. 성공/실패 처리 제거 후 별도 분리
        return cp -> {
            int remainDays = getRemainDays(cp.getCreatedAt(), currentDateTime);
            LocalDateTime weekStartDateTime = currentDateTime.minusDays(7 - remainDays);
            Long insightCount = insightQueryDomainService.countInsightCreatedAtBetween(cp, weekStartDateTime, currentDateTime);
            int remainInsightCount = (int) (cp.getInsightPerWeek() - insightCount);

            if (remainDays < remainInsightCount) {
                // 실패
                cp.expire(LocalDate.now().minusDays(1L));
            } else if (isSuccess(currentDate, cp.getEndDate(), remainInsightCount)) {
                // 성공
                cp.complete();
            }
            int currentWeek = DateTimeUtils.getWeekCountByRange(cp.getCreatedAt(), cp.getEndDate().atTime(LocalTime.MIDNIGHT), currentDateTime);
            record(cp, currentWeek, remainInsightCount);
            return cp;
        };
    }

    private boolean isSuccess(LocalDate currentDate, LocalDate challengeEndDate, int remainInsightNumber) {
        return remainInsightNumber == 0 && isLastWeek(currentDate, challengeEndDate);
    }

    private boolean isLastWeek(LocalDate startDate, LocalDate endDate) {
        return Duration.between(startDate, endDate).toDays() < 7;
    }

    private int getRemainDays(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return (7 - Math.abs(startDateTime.getDayOfWeek().getValue() - endDateTime.getDayOfWeek().getValue())) % 7;
    }

    private void record(ChallengeParticipation challengeParticipation, int weekCount, int insightCount) {
        Optional<ChallengeRecord> challengeRecordOps = challengeRecordRepository.findByChallengeParticipationAndWeekCount(challengeParticipation, weekCount);
        if (challengeRecordOps.isPresent()) {
            ChallengeRecord challengeRecord = challengeRecordOps.get();
            challengeRecord.updateRecordCount(insightCount);
            challengeRecordRepository.save(challengeRecord);
            return;
        }
        ChallengeRecord challengeRecord = ChallengeRecord.initialize(challengeParticipation, weekCount);
        challengeRecord.updateRecordCount(insightCount);
        challengeRecordRepository.save(challengeRecord);
    }
}
