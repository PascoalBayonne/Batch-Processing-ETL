package pt.com.bayonnesensei.salesInfo.batch.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomStepExecutionListener implements StepExecutionListener {
    private final JdbcTemplate jdbcTemplate;
    private static final String DELETE_QUERY = "DELETE FROM sales_info WHERE id > 0";

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("delete all old records from");
       int deletedRows = jdbcTemplate.update(DELETE_QUERY);
       log.info("Before Step we have deleted : {} records",deletedRows);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        //delete the directory
        if (ExitStatus.COMPLETED.equals(stepExecution.getExitStatus())){
            log.info("The step has finished with status: {}", stepExecution.getExitStatus());
            if (stepExecution.getSkipCount() > 0) {
                return new ExitStatus("COMPLETED WITH SKIPS");
            }
            return stepExecution.getExitStatus();
        }
        log.info("Something bad has happened after step: {}", stepExecution.getExitStatus());
        return stepExecution.getExitStatus();
    }
}
