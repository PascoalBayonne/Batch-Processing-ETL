package pt.bayonnesensei.vehicles.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class TaskExecConfiguration {

    @Bean
    public TaskExecutor parallelTaskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("TASK-");
        taskExecutor.setConcurrencyLimit(10);
        return taskExecutor;

    }
}
