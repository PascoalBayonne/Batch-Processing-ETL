package com.pascoal.app.processor;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Component
public class MoveFileToProcess implements Tasklet, InitializingBean {
    private static final Logger LOGGER = Logger.getLogger(MoveFileToProcess.class.getName());

    private Path fileWaitingLocation = Paths.get("C:\\spring.batch.resources\\waiting\\customers.csv");
    private Path fileProcessingLocation = Paths.get("C:\\spring.batch.resources\\processing\\");

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        try {
            Path processingFolder = fileProcessingLocation.toAbsolutePath();

            if (Files.notExists(processingFolder)) {
                Files.createDirectory(fileProcessingLocation);
            }
            Files.move(fileWaitingLocation, fileProcessingLocation.resolve(fileWaitingLocation.getFileName()));

            LOGGER.info("Moving file: " + fileWaitingLocation.getFileName() + " from waiting to :" + fileProcessingLocation.toAbsolutePath());
        } catch (IOException e) {
            LOGGER.info("A problem occured while moving the file: " + fileWaitingLocation.getFileName() + " from waiting to :" + fileProcessingLocation.toAbsolutePath());

        }

        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(fileWaitingLocation, "processing directory can never be null! Must be set.");
    }

}
