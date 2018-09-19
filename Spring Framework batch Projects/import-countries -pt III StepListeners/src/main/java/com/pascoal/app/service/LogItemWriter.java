package com.pascoal.app.service;

import com.pascoal.app.processor.CustomProcessor;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


public class LogItemWriter implements ItemWriter<Map<String, String>>{
    private StepExecution stepExecution;
    private Map<String, String> countryAndNationalitiesCodeMap;
    private static final Logger LOGGER = Logger.getLogger(CustomProcessor.class.getName());


    @Override
    public void write(List<? extends Map<String, String>> items) throws Exception {
        countryAndNationalitiesCodeMap = new ConcurrentHashMap<>();
        for (Map<String, String> itemElement : items) {
            itemElement.forEach((itemKey, itemValue) -> countryAndNationalitiesCodeMap.put(itemKey, itemValue));
        }
        LOGGER.info("@after step: saving data in context");
        ExecutionContext stepContext = this.stepExecution.getExecutionContext();
        stepContext.put("keys", countryAndNationalitiesCodeMap);
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
}
