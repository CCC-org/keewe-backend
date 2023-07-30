package ccc.keewebatch.helper;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

public class UniqueRunIdIncrementer implements JobParametersIncrementer {
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
