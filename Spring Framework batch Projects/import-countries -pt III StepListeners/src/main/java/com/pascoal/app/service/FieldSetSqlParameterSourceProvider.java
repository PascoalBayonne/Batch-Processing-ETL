package com.pascoal.app.service;

import com.pascoal.app.processor.MoveFileToProcess;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Logger;


public class FieldSetSqlParameterSourceProvider implements ChunkListener, ItemSqlParameterSourceProvider<Map<String, Object>> {
    private static final Logger LOGGER = Logger.getLogger(MoveFileToProcess.class.getName());
    private Path fileProcessingLocation = Paths.get("C:\\spring.batch.resources\\processing\\customers.csv");
    private Path fileProcessedLocation = Paths.get("C:\\spring.batch.resources\\processed\\");

    @Override
    public SqlParameterSource createSqlParameterSource(Map<String, Object> item) {
        LOGGER.info("persisting item into dataBase: "+item);
        return new MapSqlParameterSource(item);

    }

    @Override
    public void beforeChunk(ChunkContext context) {

    }

    @Override
    @AfterChunk
    public void afterChunk(ChunkContext context) {
        LOGGER.info("The Job is Done!! records have been persisted into DataBase");
        LOGGER.info("NOW CLOSING THE JOB");

        try {
            Path movedFile = Files.move(fileProcessingLocation, fileProcessedLocation.resolve(fileProcessingLocation).getFileName());
            File file = movedFile.toFile();
            file.renameTo(new File(fileProcessingLocation.toString()+"_processed"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterChunkError(ChunkContext context) {



    }
}
