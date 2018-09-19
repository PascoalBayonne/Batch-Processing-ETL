package com.pascoal.app.customListeners;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public class CustomChunkListener implements ChunkListener ,InitializingBean {
    private static final Logger LOGGER = Logger.getLogger(CustomChunkListener.class.getName());

    private Path fileProcessingLocation;
    private Path processedDirectory;

    @Override
    public void beforeChunk(ChunkContext context) {
        LOGGER.info("#########Executing the last HAHAHAHAHAHAHAHAHAHAHAH ");
    }

    @Override
    public void afterChunk(ChunkContext context) {
        try {

            Files.move(fileProcessingLocation.toAbsolutePath(), processedDirectory.resolve(fileProcessingLocation.getFileName() + "_processed"));
        } catch (IOException e) {
            LOGGER.info("An error occurred while trying to move the file: " + processedDirectory.toAbsolutePath() + "\n" + e);
        }
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        LOGGER.info("@@@@@@@@ FUCK SOMETHING JUST WENT WRONG, PLEASE MOVE IT TO ERROR FILE");

    }

    public void setFileProcessingLocation(Path fileProcessingLocation) {
        this.fileProcessingLocation = fileProcessingLocation;
    }

    public void setProcessedDirectory(Path processedDirectory) {
        this.processedDirectory = processedDirectory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(fileProcessingLocation,"file to process can never be null! It doesn't exists or is not readable");
        Assert.notNull(processedDirectory,"file to process can never be null! It doesn't exists or is not readable");
    }
}
