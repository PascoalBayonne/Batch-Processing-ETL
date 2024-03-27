package pt.bayonnesensei.salesmanager.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.integration.chunk.RemoteChunkingManagerStepBuilderFactory;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import pt.bayonnesensei.salesmanager.dto.SalesInfoDTO;

@Profile("manager")
@Configuration
@EnableBatchProcessing
@EnableBatchIntegration
@RequiredArgsConstructor
public class SalesJobManagerConfig {

    private final RemoteChunkingManagerStepBuilderFactory remoteChunkingManagerStepBuilderFactory;

    private final KafkaTemplate<String, SalesInfoDTO> salesInfoKafkaTemplate;

    @Bean
    public Job salesManagerJob(JobRepository jobRepository, Step salesManagerStep){
        return new JobBuilder("sales-info",jobRepository)
                .start(salesManagerStep)
                .build();
    }

    @Bean
    public TaskletStep salesManagerStep(){
        return this.remoteChunkingManagerStepBuilderFactory.get("import sales info")
                .<SalesInfoDTO,SalesInfoDTO>chunk(20)
                .reader(salesInfoReader())
                .outputChannel(outboundRequests()) // requests sent to workers
                .inputChannel(inboundReplies()) // replies received from workers
                .allowStartIfComplete(Boolean.TRUE)
                .build();
    }




    public QueueChannel inboundReplies(){
        return new QueueChannel();
    }

    @Bean
    public IntegrationFlow inboundFlow(ConsumerFactory<String, SalesInfoDTO> cf) {
        return IntegrationFlow
                .from(Kafka.messageDrivenChannelAdapter(cf, "salesInfo-chunkReplies")) //consuming from kafka
                .log(LoggingHandler.Level.WARN)
                .channel(inboundReplies())
                .get();
    }


    @Bean
    public DirectChannel outboundRequests() {
        return new DirectChannel();
    }


    @Bean
    public IntegrationFlow outboundFlow() {
        var messageHandler = new KafkaProducerMessageHandler<String,SalesInfoDTO>(salesInfoKafkaTemplate);
        messageHandler.setTopicExpression(new LiteralExpression("salesInfo-chunkRequests"));
        return IntegrationFlow.from(outboundRequests())
                .log(LoggingHandler.Level.WARN)
                .handle(messageHandler)
                .get();
    }

    public FlatFileItemReader<SalesInfoDTO> salesInfoReader(){
        return new FlatFileItemReaderBuilder<SalesInfoDTO>()
                .resource(new ClassPathResource("/files/sales-info.csv"))
                .name("salesInfoReader")
                .delimited()
                .delimiter(",")
                .names("product", "seller", "sellerId", "price", "city", "category")
                .linesToSkip(1)//skipping the header of file
                .targetType(SalesInfoDTO.class)
                .build();
    }

}
