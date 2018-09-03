package com.pascoal.app.service;

import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.LinkedHashMap;
import java.util.Map;


public class FieldSetSqlParameterSourceProvider implements ItemSqlParameterSourceProvider<FieldSet> {

    private MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

    public SqlParameterSource createSqlParameterSource(FieldSet item) {

        Map<String, Object> stringMap = new LinkedHashMap<>();

        for (Map.Entry<Object, Object> entry : item.getProperties().entrySet()) {
            stringMap.put((String) entry.getKey(), entry.getValue());
        }
        mapSqlParameterSource.addValues(stringMap);
        return mapSqlParameterSource;
    }

}
