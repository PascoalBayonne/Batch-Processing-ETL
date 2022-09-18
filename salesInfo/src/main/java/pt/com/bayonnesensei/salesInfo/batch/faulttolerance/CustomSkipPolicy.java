package pt.com.bayonnesensei.salesInfo.batch.faulttolerance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

@Component
@Slf4j
public class CustomSkipPolicy implements SkipPolicy {

    private final Integer skipLimit = 4;
    @Override
    public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {
        if (exception instanceof FileNotFoundException){
            return Boolean.FALSE;
        }else if ((exception instanceof FlatFileParseException) && (skipCount <= skipLimit) ){

            FlatFileParseException fileParseException = (FlatFileParseException) exception;
            String input = fileParseException.getInput();
            int lineNumber = fileParseException.getLineNumber();

            log.warn("The line with error is: {}",input);
            log.warn("The line number with error is: {}",lineNumber);
            //write into a file
            //send into kafka topic or any Message broker
            return Boolean.TRUE;
        }else if ((exception instanceof IllegalArgumentException) && (skipCount <= skipLimit) ){
            log.warn("An error occurred");
            return Boolean.FALSE;
        }
        return false;
    }
}
