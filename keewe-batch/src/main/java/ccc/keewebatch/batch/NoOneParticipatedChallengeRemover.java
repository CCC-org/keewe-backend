package ccc.keewebatch.batch;

import ccc.keewecore.utils.ListUtils;
import ccc.keewedomain.persistence.domain.challenge.Challenge;
import ccc.keewedomain.persistence.repository.utils.CursorPageable;
import ccc.keewedomain.service.challenge.command.ChallengeCommandDomainService;
import ccc.keewedomain.service.challenge.query.ChallengeQueryDomainService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class NoOneParticipatedChallengeRemover {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ChallengeQueryDomainService challengeQueryDomainService;
    private final ChallengeCommandDomainService challengeCommandDomainService;

    // const zone
    private static final String JOB_NAME = "noOneParticipatedChallengeRemoveJob";
    private static final Long CHUCK_SIZE = 100L;

    static class UniqueRunIdIncrementer implements JobParametersIncrementer {
        private String runId = "run.id";

        @Override
        public JobParameters getNext(JobParameters parameters) {
            JobParameters params = parameters != null ? parameters : new JobParameters();
            Long id = params.getLong(runId) != null ? params.getLong(runId) : 0L;
            return new JobParametersBuilder()
                    .addLong(runId, id + 1)
                    .toJobParameters();
        }
    }

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
    public Step noOneParticipateChallengeRemoveStep() {
        return stepBuilderFactory.get("noOneParticipateChallengeRemoveStep")
                .tasklet((contribution, chunkContext) -> {
                    Long cursor = Long.MAX_VALUE;
                    Long deletedCount = 0L;
                    do {
                        List<Challenge> challenges = challengeQueryDomainService.paginateWithParticipation(CursorPageable.of(cursor, CHUCK_SIZE));
                        List<Challenge> deletedChallenges = challenges.stream()
                                .filter(Challenge::isNoOneParticipated)
                                .map(Challenge::delete)
                                .collect(Collectors.toList());
                        challengeCommandDomainService.saveAll(deletedChallenges);
                        deletedCount += deletedChallenges.size();
                        cursor = this.getNextCursor(challenges);
                    } while (cursor != null);
                    afterDeleteChallenge(deletedCount);
                    return RepeatStatus.FINISHED;
                })
                .transactionManager(new ResourcelessTransactionManager())
                .build();
    }

    private Long getNextCursor(List<Challenge> challenges) {
        if(challenges.size() >= CHUCK_SIZE) {
            return ListUtils.getLast(challenges).getId();
        } else {
            return null;
        }
    }

    private void afterDeleteChallenge(Long deletedCount) {
        // TODO(호성): 삭제 결과 건 수 슬랙 메시지 발송
        log.info("[NoOneParticipatedChallengeRemover] 총 ({})건 처리", deletedCount);
    }
}
