package pt.com.bayonnesensei.salesInfo.batch.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

@Slf4j
public class CustomJobDecider implements JobExecutionDecider {
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if (stepExecution.getExitStatus().getExitCode().equalsIgnoreCase(ExitStatus.FAILED.getExitCode())
            && stepExecution.getReadSkipCount() > 1) {
            log.info("Changing the exit status to COMPLETED WITH SKIPS");
            return new FlowExecutionStatus("COMPLETED WITH SKIPS");
        }
        String exitCode = stepExecution.getExitStatus().getExitCode();
        log.warn(exitCode);
        return new FlowExecutionStatus(exitCode);
    }
}
