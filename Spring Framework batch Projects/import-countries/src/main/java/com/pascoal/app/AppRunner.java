package com.pascoal.app;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.logging.Logger;

public class AppRunner {
    private static final Logger LOGGER = Logger.getLogger(AppRunner.class.getName());

    public static void main(String[] args) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        String[] springConfigurations = {"batch-core.xml", "jobConfig.xml"};

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(springConfigurations);

        //Creating jobLauncher
        JobLauncher jobLauncher = (JobLauncher) applicationContext.getBean("jobLauncher");

        //creating the Job
        Job importCustomerJob = (Job) applicationContext.getBean("import-customer");

        //executing the job
        JobExecution jobExecution = jobLauncher.run(importCustomerJob, new JobParameters());

        LOGGER.info("Exit status: " + jobExecution.getStatus());

    }
}
