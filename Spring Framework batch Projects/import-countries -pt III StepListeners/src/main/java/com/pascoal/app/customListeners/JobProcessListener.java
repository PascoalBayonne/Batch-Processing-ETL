package com.pascoal.app.customListeners;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class JobProcessListener implements JobExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(JobProcessListener.class);
    private Path fileToProcessesLocation;
    private Path processedFolderLocation;
    private Path fileInProcess;
    private Path errorFolder;


    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("validating resource file");
        if (!isFileReadyToBeProcessed(fileToProcessesLocation)) {
            throw new IllegalStateException("file to process doesn't exist");
        }
        logger.info("Job is starting");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        logger.info("job finished.[JobName:{}][ExitStatus:{}]" +
                        jobExecution.getJobInstance().getJobName(),
                jobExecution.getExitStatus().getExitCode());

        String exitCode = jobExecution.getExitStatus().getExitCode();
        computeFileDestinationDecision(exitCode);

        jobExecution.getStepExecutions().forEach(stepExecution -> {
            Object errorItem = stepExecution.getExecutionContext().get("ERROR_ITEM");
            if (errorItem != null) {
                logger.error("Check the error folder because it was detected some errors on this items while processing. " +
                                "[step:{}] [item:{}]", stepExecution.getStepName(),
                        errorItem);
            }
        });
    }

    private boolean isFileReadyToBeProcessed(Path pathFile) {
        File fileToProcess = pathFile.toFile();
        return (Files.exists(pathFile) && fileToProcess.exists());
    }

    private void computeFileDestinationDecision(String jobExitCode) {
        Assert.notNull(jobExitCode, "unexpected error! Job execution is null or related problem :( ");
        if (CustomExitCode.COMPLETED.codeDescription.equalsIgnoreCase(jobExitCode)) {
            moveFileToProcessedFolder(fileInProcess, processedFolderLocation);
        } else if (CustomExitCode.FAILED.codeDescription.equalsIgnoreCase(jobExitCode)) {
            moveFileToErrorFolder(fileInProcess, errorFolder);
        }
    }

    private void moveFileToProcessedFolder(Path fileInProcess, Path processedFolderLocation) {
        logger.info("Moving the processed file to new folder: PROCESSED_DIRECTORY");
        try {
            Files.move(fileInProcess.toAbsolutePath(), processedFolderLocation.resolve(fileInProcess.getFileName() + "_PROCESSED"));
        } catch (IOException e) {
            logger.info("Error while trying to move the processed file: ");
            logger.error(e.getMessage());
        }
    }

    private void moveFileToErrorFolder(Path fileInProcess, Path errorFolder) {
        logger.info("Unexpected error occurred while the job execution! Now moving file to error folder");
        try {
            //if in processing folder doesn't exists we find it in another folder
            Files.move(fileInProcess.toAbsolutePath(), errorFolder.resolve(fileInProcess.getFileName() + "_ERROR"));
        } catch (IOException e) {
            logger.info("Error while trying to move the processed file: ");
            logger.error(e.getMessage());
            try {

                Files.move(fileToProcessesLocation.toAbsolutePath(), errorFolder.resolve(fileInProcess.getFileName() + "_ERROR"));
            } catch (IOException e1) {
                logger.error(e1.getMessage());
            }
        }
    }

    public enum CustomExitCode {
        COMPLETED("COMPLETED"), FAILED("FAILED"), STOPPED("STOPPED"), NOOP("STOPPED");

        String codeDescription;

        CustomExitCode(String codeDescription) {
            this.codeDescription = codeDescription;
        }
    }

    public void setFileToProcessesLocation(Path fileToProcessesLocation) {
        this.fileToProcessesLocation = fileToProcessesLocation;
    }

    public void setProcessedFolderLocation(Path processedFolderLocation) {
        this.processedFolderLocation = processedFolderLocation;
    }

    public void setFileInProcess(Path fileInProcess) {
        this.fileInProcess = fileInProcess;
    }

    public void setErrorFolder(Path errorFolder) {
        this.errorFolder = errorFolder;
    }
}
