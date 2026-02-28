package pt.bayonnesensei.vehicles.job.steps.mercedes;

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
public class MercedesVehiclesStepConfiguration {

    private final VehicleReader vehicleReader;
    private final VehicleWriter vehicleWriter;

    private static final String TASK_NAME = "importMercedesVehicles";


    @Bean
    public Step importMercedesVehiclesStep(JobRepository jobRepository) {
        return new TaskletStepBuilder(
                new StepBuilder(TASK_NAME, jobRepository))
                .tasklet(new Tasklet() {
                    @Override
                    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

                        List<VehicleDTO> vehicles = vehicleReader.fetchVehicles("/api/vehicles/mercedes");

                        int[][] ints = vehicleWriter.vehicleBatchItemWriter(vehicles);

                        int totalRecordsInserted = Arrays.stream(ints).flatMapToInt(Arrays::stream).sum();

                        log.info("Task {}. Persisted {}", TASK_NAME, totalRecordsInserted);
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }
}
