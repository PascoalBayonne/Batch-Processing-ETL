package com.pascoal.app.customListeners;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public class StepListener implements StepExecutionListener {
    private static final Logger LOGGER = Logger.getLogger(StepListener.class.getName());


    private Path fileProcessingLocation;
    private Path processedDirectory;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        LOGGER.info("#########Executing the last step ");

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        try {

            Files.move(fileProcessingLocation.toAbsolutePath(), processedDirectory.resolve(fileProcessingLocation.getFileName() + "_processed"));
        } catch (IOException e) {
            LOGGER.info("An error occurred while trying to move the file: " + processedDirectory.toAbsolutePath() + "\n" + e);
        }
        return ExitStatus.COMPLETED;
    }

    public void setProcessedDirectory(Path processedDirectory) {
        this.processedDirectory = processedDirectory;
    }

    public void setFileProcessingLocation(Path fileProcessingLocation) {
        this.fileProcessingLocation = fileProcessingLocation;
    }
}
