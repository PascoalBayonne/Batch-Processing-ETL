package pt.com.bayonnesensei.salesInfo.batch.listeners;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class CustomJobExecutionListener implements JobExecutionListener {

    public static final String INPUT_FILE_NAME = "input.file.name";

    @Override
    @SneakyThrows
    public void beforeJob(JobExecution jobExecution) {
        log.info("---------------> Before job execution");
        JobParameters jobParameters = jobExecution.getJobParameters();
        Map<String, JobParameter> parameters = jobParameters.getParameters();
        if (parameters.containsKey(INPUT_FILE_NAME)){
            JobParameter inputFileAsJobParameter = parameters.get(INPUT_FILE_NAME);
            Path inputDirectoryPath = Paths.get(inputFileAsJobParameter.getValue().toString()).getParent();
            Path processedDirectory = Path.of(inputDirectoryPath.toFile() + File.separator + "processed");
            if (Files.notExists(processedDirectory)){
                Files.createDirectory(processedDirectory);
            }
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("-----------------> After job computing the business logic");
        JobParameters jobParameters = jobExecution.getJobParameters();
        Map<String, JobParameter> parameters = jobParameters.getParameters();
        if (parameters.containsKey(INPUT_FILE_NAME)) compute(jobExecution, parameters);
    }

    private void compute(final JobExecution jobExecution, Map<String, JobParameter> parameters) {
        Path inputDirectoryAbsolutePath = Path.of((String) parameters.get(INPUT_FILE_NAME).getValue());
        Path inputDirectoryParent = inputDirectoryAbsolutePath.getParent();

        Path processedPath = Paths.get(inputDirectoryParent + File.separator + "processed");
        Path failedPath = Paths.get(inputDirectoryParent + File.separator + "failed");

        if (ExitStatus.COMPLETED.getExitCode().equalsIgnoreCase(jobExecution.getExitStatus().getExitCode())){
            createDirectoryIfAbsent(processedPath);
            computeFileMove(inputDirectoryAbsolutePath,processedPath);
        }
        if (ExitStatus.STOPPED.getExitCode().equalsIgnoreCase(jobExecution.getExitStatus().getExitCode()) || ExitStatus.FAILED.getExitCode().equalsIgnoreCase(jobExecution.getExitStatus().getExitCode())){
            createDirectoryIfAbsent(failedPath);
            computeFileMove(inputDirectoryAbsolutePath,failedPath);
        }
    }

    @SneakyThrows
    void computeFileMove(final Path inputDirectoryAbsolutePath, final Path targetDirectory){
        Path destination = targetDirectory.resolve(inputDirectoryAbsolutePath.getFileName());
        Files.move(inputDirectoryAbsolutePath, destination, StandardCopyOption.ATOMIC_MOVE);
    }

    @SneakyThrows
    void createDirectoryIfAbsent(final Path directoryPath){
        Objects.requireNonNull(directoryPath, "the directory path cannot be null");
        if (Files.notExists(directoryPath)){
            log.warn("------------> creating directory: {}",directoryPath.toAbsolutePath());
            Files.createDirectory(directoryPath);
        }
    }
}
