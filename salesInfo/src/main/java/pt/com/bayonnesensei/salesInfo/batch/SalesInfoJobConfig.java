package pt.com.bayonnesensei.salesInfo.batch;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import pt.com.bayonnesensei.salesInfo.batch.dto.SalesInfoDTO;
import pt.com.bayonnesensei.salesInfo.batch.faulttolerance.CustomSkipPolicy;
import pt.com.bayonnesensei.salesInfo.batch.listeners.CustomJobExecutionListener;
import pt.com.bayonnesensei.salesInfo.batch.listeners.CustomStepExecutionListener;
import pt.com.bayonnesensei.salesInfo.batch.processor.SalesInfoItemProcessor;
import pt.com.bayonnesensei.salesInfo.batch.step.FileCollector;
import pt.com.bayonnesensei.salesInfo.batch.step.SendEmail;
import pt.com.bayonnesensei.salesInfo.domain.SalesInfo;

import javax.persistence.EntityManagerFactory;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@RequiredArgsConstructor
public class SalesInfoJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final SalesInfoItemProcessor salesInfoItemProcessor;
    private final CustomSkipPolicy customSkipPolicy;
    private final CustomStepExecutionListener customStepExecutionListener;
    private final CustomJobExecutionListener customJobExecutionListener;
    private final KafkaTemplate<String, SalesInfo> salesInfoKafkaTemplate;
    private final FileCollector fileCollector;

    private final SendEmail sendEmail;


    @Bean
    public Job importSalesInfo(Step fromFileIntoKafka) {
        return jobBuilderFactory.get("importSalesInfo")
                .incrementer(new RunIdIncrementer())
                .start(fromFileIntoKafka).on("FAILED").fail()
                .from(fromFileIntoKafka).on("COMPLETED").to(fileCollectorTasklet())
                .from(fromFileIntoKafka).on("COMPLETED WITH SKIPS").to(sendEmailTasklet())
                .end()
                .listener(customJobExecutionListener)
                .build();
    }


    @Bean(name = "fromFileIntoKafka")
    public Step fromFileIntoKafka(ItemReader<SalesInfoDTO> salesInfoDTOItemReader) {
        return stepBuilderFactory.get("fromFileIntoDatabase")
                .<SalesInfoDTO, Future<SalesInfo>>chunk(100)
                .reader(salesInfoDTOItemReader)
                .processor(asyncItemProcessor())
                .writer(asyncItemWriter())
                .faultTolerant()
                .skipPolicy(customSkipPolicy)
                .listener(customStepExecutionListener)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<SalesInfoDTO> salesInfoFileReader(@Value("#{jobParameters['input.file.name']}") String resource) {
        return new FlatFileItemReaderBuilder<SalesInfoDTO>()
                .resource(new FileSystemResource(resource))
                .name("salesInfoFileReader")
                .delimited()
                .delimiter(",")
                .names("product", "seller", "sellerId", "price", "city", "category")
                .linesToSkip(1)//skipping the header of file
                .targetType(SalesInfoDTO.class)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(1);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("Thread N-> :");
        return executor;//used on multithreaded step
    }

    @Bean
    public AsyncItemProcessor<SalesInfoDTO, SalesInfo> asyncItemProcessor() {
        var asyncItemProcessor = new AsyncItemProcessor<SalesInfoDTO, SalesInfo>();
        asyncItemProcessor.setDelegate(salesInfoItemProcessor);
        asyncItemProcessor.setTaskExecutor(taskExecutor());
        return asyncItemProcessor;
    }

    @Bean
    public AsyncItemWriter<SalesInfo> asyncItemWriter() {
        var asyncWriter = new AsyncItemWriter<SalesInfo>();
        asyncWriter.setDelegate(salesInfoJpaItemWriter());
        return asyncWriter;
    }

    @Bean
    public JpaItemWriter<SalesInfo> salesInfoJpaItemWriter(){
        return new JpaItemWriterBuilder<SalesInfo>().entityManagerFactory(entityManagerFactory)
                .usePersist(Boolean.TRUE)
                .build();
    }

    @Bean
    public Step fileCollectorTasklet() {
        return stepBuilderFactory.get("fileCollector")
                .tasklet(fileCollector)
                .build();
    }

    @Bean
    public Step sendEmailTasklet() {
        return stepBuilderFactory.get("send email tasklet step")
                .tasklet(sendEmail)
                .build();
    }
}
