package pt.com.bayonnesensei.salesInfo.batch.integration;

import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.MessageHandler;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.launch.JobLaunchingGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.DefaultFileNameGenerator;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Duration;

@Component
@EnableIntegration
@IntegrationComponentScan
@RequiredArgsConstructor
public class SalesInfoIntegrationConfig {

    private final Job importSalesInfo;
    private final JobRepository jobRepository;

    @Value("${sales.info.directory}")
    private String salesDirectory;

    @Bean
    public IntegrationFlow integrationFlow(){
        return IntegrationFlows.from(fileReadingMessageSource(),
                sourcePolling -> sourcePolling.poller(Pollers.fixedDelay(Duration.ofSeconds(5)).maxMessagesPerPoll(1)))
                .channel(fileIn())
                .handle(fileRenameProcessingHandler())
                .transform(fileMessageToJobRequest())
                .handle(jobLaunchingGateway())
                .log()
                .get();
    }



    public FileReadingMessageSource fileReadingMessageSource(){
       var messageSource = new FileReadingMessageSource();
       messageSource.setDirectory(new File(salesDirectory));
       messageSource.setFilter(new SimplePatternFileListFilter("*.csv"));
       return messageSource;
    }

    public DirectChannel fileIn(){
        return new DirectChannel();
    }

    public FileWritingMessageHandler fileRenameProcessingHandler(){
        var fileWritingMessage = new FileWritingMessageHandler(new File(salesDirectory));
        fileWritingMessage.setFileExistsMode(FileExistsMode.REPLACE);
        fileWritingMessage.setDeleteSourceFiles(Boolean.TRUE);
        fileWritingMessage.setFileNameGenerator(new DefaultFileNameGenerator());
        fileWritingMessage.setFileNameGenerator(fileNameGenerator());
        fileWritingMessage.setRequiresReply(Boolean.FALSE);
        return fileWritingMessage;
    }

    public DefaultFileNameGenerator fileNameGenerator(){
        var filenameGenerator = new DefaultFileNameGenerator();
        filenameGenerator.setExpression("payload.name + '.processing'");
        return filenameGenerator;
    }

    public FileMessageToJobRequest fileMessageToJobRequest(){
        var transformer = new FileMessageToJobRequest();
        transformer.setJob(importSalesInfo);
        transformer.setFilename("input.file.name");
        return transformer;
    }

    public JobLaunchingGateway jobLaunchingGateway(){
        var simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        simpleJobLauncher.setTaskExecutor(new SyncTaskExecutor());
        return new JobLaunchingGateway(simpleJobLauncher);
    }


}
