package com.pascoal.app.processor;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.FieldSet;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class CustomProcessor implements ItemProcessor<FieldSet, Map<String, Object>> {
    private static final Logger LOGGER = Logger.getLogger(CustomProcessor.class.getName());
    private static final String NATIONALITY_CODE_COLUMN = "nationalityCode";
    private static final String NATIONALITY_COLUMN = "nationality";
    private static final String UNKNOWN_COUNTRY = "n/a";
    private Map<String, Object> countriesAndNationsCodeMap;

    public Map<String, Object> process(FieldSet item) {
        LOGGER.info("@processing row "+item.toString());
        String nationalityCode = item.readRawString(NATIONALITY_CODE_COLUMN);
        Map<String, Object> itemsMap = new ConcurrentHashMap<>();
        item.getProperties().forEach((itemKey, itemValue) -> itemsMap.put(itemKey.toString(), itemValue));

        computeIfEmptyNationality(itemsMap);
        if (nationalityCode.isEmpty()) {
            Object currentNationality = itemsMap.get(NATIONALITY_COLUMN);
            fetchAndComputeNationalities(currentNationality.toString(), itemsMap);
        }
        return itemsMap;
    }

    @BeforeStep
    public void retrieveInterStepData(StepExecution stepExecution) {
        LOGGER.info("@BeforeStep:  retrieving data from last step");
        JobExecution jobExecution = stepExecution.getJobExecution();
        Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
        stepExecutions.stream().map(StepExecution::getExecutionContext)
                .filter(executionContext -> executionContext.containsKey("keys"))
                .forEach(executionContext -> this.countriesAndNationsCodeMap = (Map<String, Object>) executionContext.get("keys"));
    }

    /* Replaces the nationality if it's empty. replace(emptyNationality with n/a which means not available)
    @NATIONALITY_COLUMN is the name of the column in database : which value is expected any country if its not empty */
    private void computeIfEmptyNationality(Map<String, Object> fieldsInMap) {
        Object nationality = fieldsInMap.get(NATIONALITY_COLUMN);
        if (fieldsInMap.get(NATIONALITY_COLUMN) == null || ValidationFields.EMPTY_NATIONALITY.value.equalsIgnoreCase(nationality.toString())) {
            fieldsInMap.replace(NATIONALITY_COLUMN, UNKNOWN_COUNTRY);
        }
    }

    private void fetchAndComputeNationalities(String nationality, Map<String, Object> fieldsInMap) {
        Objects.requireNonNull(nationality, "nationality code cannot be null");
        Object countryCode;

        if (!UNKNOWN_COUNTRY.equalsIgnoreCase(nationality)) {
            countryCode = countriesAndNationsCodeMap.computeIfPresent(nationality, (countryAsKey, countryCodeAsValue) -> countryCodeAsValue);
            fieldsInMap.replace(NATIONALITY_CODE_COLUMN, countryCode);

        } else {
            fieldsInMap.replace(NATIONALITY_CODE_COLUMN, UNKNOWN_COUNTRY);
        }
    }


    public enum ValidationFields {
        EMPTY_NATIONALITY(""), KNOWN("known");
        String value;

        ValidationFields(String value) {
            this.value = value;
        }
    }
}
