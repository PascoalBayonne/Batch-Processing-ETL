package com.pascoal.app.service;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class StringRowMapper implements RowMapper<Map<String, String>> {
    @Override
    public Map<String, String> mapRow(ResultSet resultSet, int i) throws SQLException {

        String country = resultSet.getString("country");
        String countryCode = resultSet.getString("countryCode");

        // String[] token = {id, country,countryCode};
        Map<String, String> resultSetInMap = new LinkedHashMap<>();
        resultSetInMap.put(country, countryCode);

        System.out.println("Converting the rows in a map: " + resultSetInMap);
        return resultSetInMap;
    }
}
