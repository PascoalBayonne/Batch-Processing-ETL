package pt.com.bayonnesensei.salesInfo.batch.step;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendEmail implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        //send email
        log.warn("-----------> Sending email on COMPLETED WITH SKIPS");
        int readSkipCount = contribution.getReadSkipCount();
        log.info("-----------> The job has completed but {} lines skipped",readSkipCount);
        return RepeatStatus.FINISHED;
    }
}
