package com.pascoal.app.service;

import com.pascoal.app.processor.MoveFileToProcess;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Map;
import java.util.logging.Logger;


public class FieldSetSqlParameterSourceProvider implements ItemSqlParameterSourceProvider<Map<String, Object>> {
    private static final Logger LOGGER = Logger.getLogger(MoveFileToProcess.class.getName());

    @Override
    public SqlParameterSource createSqlParameterSource(Map<String, Object> item) {
        LOGGER.info("persisting item into dataBase: " + item);
        return new MapSqlParameterSource(item);
    }
}
