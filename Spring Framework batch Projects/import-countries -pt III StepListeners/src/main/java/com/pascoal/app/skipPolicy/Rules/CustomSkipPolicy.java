package com.pascoal.app.skipPolicy.Rules;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.NonSkippableReadException;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.transform.IncorrectTokenCountException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Component
public class CustomSkipPolicy implements SkipPolicy, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger("CustomSkipPolicy");

    private Path linesSkippedFile;
    private int skipErrorLimit;


    @Override
    public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {

        if (exception instanceof FileNotFoundException) {
            return false;
        } else if ((exception instanceof IncorrectTokenCountException || exception instanceof FlatFileParseException) && (skipCount <= skipErrorLimit)) {

            FlatFileParseException fileParseException = (FlatFileParseException) exception;
            StringBuilder errorMessage = new StringBuilder();

            errorMessage.append("ERROR: An error occurred while processing the ")
                    .append(fileParseException.getLineNumber())
                    .append(" the line of the file. See the error detail below").append(" input is: .\n");

            errorMessage.append(fileParseException.getInput()).append("\n");
            logger.error("{}", errorMessage.toString());

            try {

                if (Files.notExists(linesSkippedFile)) {
                    logger.info(String.format("The file to write skipped lines doesn't exists! Now trying to create file: %s", linesSkippedFile.toAbsolutePath()));
                    Files.createFile(linesSkippedFile);
                }

                try {
                    String inputLine = fileParseException.getInput();
                    Files.write(linesSkippedFile, (inputLine).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                } catch (IOException e) {
                    logger.info(new StringBuilder().append("An error occurred while trying to write the skipped line into the file name: ").append(e).toString());
                }

            } catch (IOException e) {
                logger.info(e.getMessage());
            }
            return true;
        }

        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(linesSkippedFile, "File doesn't exists! " + linesSkippedFile.toAbsolutePath());
    }

    public void setLinesSkippedFile(Path linesSkippedFile) {
        this.linesSkippedFile = linesSkippedFile;
    }

    public void setSkipErrorLimit(int skipErrorLimit) {
        this.skipErrorLimit = skipErrorLimit;
    }
}
