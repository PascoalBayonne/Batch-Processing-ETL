package pt.bayonnesensei.vehicles.job;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.EnableJdbcJobRepository;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;


@Configuration
@EnableBatchProcessing
@EnableJdbcJobRepository
public class VehiclesJobConfiguration {

    @Bean
    public Job importVehiclesJob(JobRepository jobRepository, Flow vehicleParallelSplit) {
        return new JobBuilder("importVehiclesJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(vehicleParallelSplit)
                .end()
                .build();
    }

    @Bean
    public Flow vehicleParallelSplit(TaskExecutor parallelTaskExecutor,
                                     Flow bmwFlow,
                                     Flow bydFlow,
                                     Flow mercedesFlow,
                                     Flow teslaFlow,
                                     Flow toyotaFlow) {
        return new FlowBuilder<Flow>("vehicleParallelSplit")
                .split(parallelTaskExecutor)
                .add(bmwFlow, bydFlow, mercedesFlow, toyotaFlow, teslaFlow)
                .build();

    }

    @Bean
    public Flow bmwFlow(@Qualifier("importBmwVehicleStep") Step importBmwVehicleStep) {
        return new FlowBuilder<Flow>("bmwFlow")
                .start(importBmwVehicleStep)
                .build();
    }

    @Bean
    public Flow bydFlow(@Qualifier("importBydVehiclesStep") Step importBydVehiclesStep) {
        return new FlowBuilder<Flow>("bydFlow")
                .start(importBydVehiclesStep)
                .build();
    }

    @Bean
    public Flow mercedesFlow(@Qualifier("importMercedesVehiclesStep") Step importMercedesVehiclesStep) {
        return new FlowBuilder<Flow>("mercedesFlow")
                .start(importMercedesVehiclesStep)
                .build();
    }

    @Bean
    public Flow teslaFlow(@Qualifier("importTeslaVehiclesStep") Step importTeslaVehiclesStep) {
        return new FlowBuilder<Flow>("teslaFlow")
                .start(importTeslaVehiclesStep)
                .build();
    }

    @Bean
    public Flow toyotaFlow(@Qualifier("importToyotaVehiclesStep") Step importToyotaVehiclesStep) {
        return new FlowBuilder<Flow>("toyotaFlow")
                .start(importToyotaVehiclesStep)
                .build();
    }


}
