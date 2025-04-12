package pt.bayonnesensei.mongodb_writer.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class CountryJobScheduler {

    private final Job job;
    private final JobLauncher jobLauncher;


    @Scheduled(cron = "0/5 * * * * *")
    @SneakyThrows
    void triggerJob() {
        var jobParameters = new JobParametersBuilder()
                .addDate("processDate", new Date(), true)
                .toJobParameters();

        jobLauncher.run(job, jobParameters);
    }


}
