package pt.bayonnesensei.vehicles.job.steps.bwm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.bayonnesensei.vehicles.dto.VehicleDTO;
import pt.bayonnesensei.vehicles.job.common.VehicleReader;
import pt.bayonnesensei.vehicles.job.common.VehicleWriter;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BmwVehicleStepConfiguration {

    private static final String TASK_NAME = "importBmwVehicles";

    private final VehicleReader vehicleReader;
    private final VehicleWriter vehicleWriter;

    @Bean
    public Step importBmwVehicleStep(JobRepository jobRepository) {
        return new TaskletStepBuilder(new StepBuilder(TASK_NAME, jobRepository))
                .tasklet(new Tasklet() {
                    @Override
                    public @Nullable RepeatStatus execute(StepContribution contribution,
                                                          ChunkContext chunkContext) throws Exception {

                        List<VehicleDTO> vehicleDTOS = vehicleReader.fetchVehicles("/api/vehicles/bmw");

                        int[][] ints = vehicleWriter.vehicleBatchItemWriter(vehicleDTOS);

                        int totalRecordsInserted = Arrays.stream(ints).flatMapToInt(Arrays::stream).sum();

                        log.info("Task {} persisted {} records", TASK_NAME, totalRecordsInserted);
                        return RepeatStatus.FINISHED;
                    }
                }).build();

    }
}
