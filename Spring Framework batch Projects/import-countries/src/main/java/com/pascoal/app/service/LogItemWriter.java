package com.pascoal.app.service;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LogItemWriter implements ItemWriter<FieldSet> {
    @Override
    public void write(List<? extends FieldSet> items) throws Exception {
        items.forEach(item-> System.out.println(item.toString()));
    }
}
