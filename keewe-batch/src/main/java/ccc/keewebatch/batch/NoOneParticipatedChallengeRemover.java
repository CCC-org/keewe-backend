package ccc.keewebatch.batch;

import ccc.keewebatch.helper.UniqueRunIdIncrementer;
import ccc.keewecore.utils.ListUtils;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.domain.challenge.ChallengeParticipation;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.challenge.command.ChallengeCommandDomainService;
import ccc.keewedomain.service.challenge.query.ChallengeParticipateQueryDomainService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class NoOneParticipatedChallengeRemover {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ChallengeParticipateQueryDomainService challengeParticipateQueryDomainService;
    private final ChallengeCommandDomainService challengeCommandDomainService;

    // const zone
    private static final String JOB_NAME = "noOneParticipatedChallengeRemoveJob";
    private static final Long CHUCK_SIZE = 100L;

    @Bean
    public Job noOneParticipatedChallengeRemoveJob(Step noOneParticipateChallengeRemoveStep) {
        return jobBuilderFactory.get(JOB_NAME)
                .preventRestart()
                .incrementer(new UniqueRunIdIncrementer())
                .start(noOneParticipateChallengeRemoveStep)
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        log.info("[NoOneParticipatedChallengeRemover] 참여 인원이 없는 챌린지 삭제 작업 시작");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        log.info("[NoOneParticipatedChallengeRemover] 참여 인원이 없는 챌린지 삭제 작업 종료");
                    }
                })
                .build();
    }

    @Bean
    @JobScope
    public Step noOneParticipateChallengeRemoveStep(
            // note. 5분 주기로 실행. [startDateTimeString <= 현재시각 - 5분] , [endDateTimeString <= 현재시각]
            @Value("#{jobParameters[startDateTimeString]}") String startDateTimeString,
            @Value("#{jobParameters[endDateTimeString]}") String endDateTimeString
    ) {
        return stepBuilderFactory.get("noOneParticipateChallengeRemoveStep")
                .tasklet((contribution, chunkContext) -> {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                    LocalDateTime startDateTime = startDateTimeString != null ? LocalDateTime.parse(startDateTimeString, formatter) : LocalDateTime.now().minusMinutes(5);
                    LocalDateTime endDateTime = endDateTimeString != null ? LocalDateTime.parse(endDateTimeString, formatter) : LocalDateTime.now();
                    Long cursor = Long.MAX_VALUE;
                    Long deletedCount = 0L;
                    do {
                        List<ChallengeParticipation> participations = challengeParticipateQueryDomainService.paginateFinished(CursorPageable.of(cursor, CHUCK_SIZE), startDateTime, endDateTime);
                        List<Challenge> challenges = participations.stream()
                                .map(ChallengeParticipation::getChallenge)
                                .filter(challenge -> !challenge.isDeleted()) // note. 이미 처리된 챌린지 SKIP
                                .distinct()
                                .collect(Collectors.toList());
                        // 삭제 처리
                        List<Challenge> deletedChallenges = challenges.stream()
                                .filter(Challenge::isNoOneParticipated)
                                .map(Challenge::delete)
                                .collect(Collectors.toList());
                        challengeCommandDomainService.saveAll(deletedChallenges);
                        deletedCount += deletedChallenges.size();
                        cursor = this.getNextCursor(participations);
                    } while (cursor != null);
                    afterDeleteChallenge(startDateTime, endDateTime, deletedCount);
                    return RepeatStatus.FINISHED;
                })
                .transactionManager(new ResourcelessTransactionManager())
                .build();
    }

    private Long getNextCursor(List<ChallengeParticipation> participations) {
        if(participations.size() >= CHUCK_SIZE) {
            return ListUtils.getLast(participations).getId();
        } else {
            return null;
        }
    }

    private void afterDeleteChallenge(LocalDateTime startDateTime, LocalDateTime endDateTime, Long deletedCount) {
        // TODO(호성): 삭제 결과 건 수 슬랙 메시지 발송
        log.info("[NoOneParticipatedChallengeRemover] [({}) - ({})] 사이에 상태가 변화한 데이터 총 ({})건 처리", startDateTime, endDateTime, deletedCount);
    }
}
