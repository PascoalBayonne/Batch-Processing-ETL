package pt.bayonnesensei.salesmanager.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.integration.chunk.RemoteChunkingWorkerBuilder;
import org.springframework.batch.integration.config.annotation.EnableBatchIntegration;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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


@Profile("worker")
@EnableBatchProcessing
@EnableBatchIntegration
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SalesWorkerConfig {
    private final RemoteChunkingWorkerBuilder<SalesInfoDTO,SalesInfoDTO> remoteChunkingWorkerBuilder;
    private final KafkaTemplate<String, SalesInfoDTO> salesInfoKafkaTemplate;

    @Bean
    public IntegrationFlow salesWorkerStep(){
        return this.remoteChunkingWorkerBuilder
                .inputChannel(inboundRequests())
                .itemProcessor(salesInfoDTO -> {
                    log.info("processing data on worker: {}", salesInfoDTO);
                    return salesInfoDTO;
                })
                .itemWriter(items -> log.info("persisting data: {}",items))
                .outputChannel(outboundRequests())
                .build();
    }

    @Bean
    public QueueChannel inboundRequests() {
        return new QueueChannel();
    }

    @Bean
    public IntegrationFlow inboundFlow(ConsumerFactory<String, SalesInfoDTO> cf) {
        return IntegrationFlow
                .from(Kafka.messageDrivenChannelAdapter(cf, "salesInfo-chunkRequests"))
                .channel(inboundRequests())
                .get();
    }



    @Bean
    public DirectChannel outboundRequests() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow outboundFlow() {
        var messageHandler = new KafkaProducerMessageHandler<String,SalesInfoDTO>(salesInfoKafkaTemplate);
        messageHandler.setTopicExpression(new LiteralExpression("salesInfo-chunkReplies"));
        return IntegrationFlow.from(outboundRequests())
                .log(LoggingHandler.Level.WARN)
                .handle(messageHandler)
                .get();
    }
}
