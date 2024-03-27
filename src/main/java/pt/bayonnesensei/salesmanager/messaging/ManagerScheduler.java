package pt.bayonnesensei.salesmanager.messaging;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Profile("manager")
@Component
public class ManagerScheduler {
    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job salesManagerJob;

    @Scheduled(cron = "* * * * * ?")
    public void perform() throws Exception
    {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(salesManagerJob, params);
    }
}
