package ccc.keewebatch.batch;

import static ccc.keewebatch.batch.ChallengeRecordChecker.JOB_NAME;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name ="job.name", havingValue = JOB_NAME)
public class NoOneParticipatedChallengeRemover {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private static final String JOB_NAME = "noOneParticipatedChallengeRemoveJob";

    @Bean
    public Job noOneParticipatedChallengeRemoveJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .preventRestart()
                .start(noOneParticipteChallengeRemoveStep())
                .build();
    }

    @Bean
    public Step noOneParticipteChallengeRemoveStep() {
        return stepBuilderFactory.get("noOneParticipteChallengeRemoveStep")
                .tasklet((contribution, stepContext) -> {
                    return RepeatStatus.FINISHED;
                })
                .transactionManager(new ResourcelessTransactionManager())
                .build();
    }
}
