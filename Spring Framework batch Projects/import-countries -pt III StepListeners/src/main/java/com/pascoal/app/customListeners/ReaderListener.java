package com.pascoal.app.customListeners;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.item.file.transform.FieldSet;

public class ReaderListener implements ItemReadListener<FieldSet> {
    @Override
    public void beforeRead() {

    }

    @Override
    public void afterRead(FieldSet item) {

    }

    @Override
    public void onReadError(Exception ex) {

    }
}
