package com.pascoal.app.service;

import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RetrieveItemWriter implements ItemWriter<Map<String, String>> {
    private Map<String, String> countryAndNationalitiesCodeMap;

    @Override
    public void write(List<? extends Map<String, String>> items) throws Exception {
        countryAndNationalitiesCodeMap = new ConcurrentHashMap<>();
        for (Map<String, String> stringMap : items) {
            stringMap.forEach(countryAndNationalitiesCodeMap::put);
        }
    }
}
