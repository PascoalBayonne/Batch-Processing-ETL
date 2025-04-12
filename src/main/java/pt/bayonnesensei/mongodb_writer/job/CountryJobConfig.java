package pt.bayonnesensei.mongodb_writer.job;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import pt.bayonnesensei.mongodb_writer.domain.Country;
import pt.bayonnesensei.mongodb_writer.dto.CountryDTO;
import pt.bayonnesensei.mongodb_writer.processor.CountryItemProcessor;
import pt.bayonnesensei.mongodb_writer.serde.CountryDTODeserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class CountryJobConfig {

    private final CountryItemProcessor countryItemProcessor;
    private final MongoTemplate mongoTemplate;
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;

    @Bean
    public Job importCountries(Step fromKafkaToMongoDb) {
        return new JobBuilder("import-countries", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(fromKafkaToMongoDb)
                .build();
    }

    @Bean
    public Step fromKafkaToMongoDb() {
        return new StepBuilder("fromKafkaToMongo", jobRepository)
                .<CountryDTO, Country>chunk(5, transactionManager)
                .reader(countryKafkaItemReader())
                .processor(countryItemProcessor)
                .writer(countryMongoItemWriter())
                .allowStartIfComplete(Boolean.TRUE)
                .build();

    }

    @Bean
    public KafkaItemReader<String, CountryDTO> countryKafkaItemReader() {
        var consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "countryJob");
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CountryDTODeserializer.class);
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new KafkaItemReaderBuilder<String, CountryDTO>()
                .name("country-item-reader")
                .topic("countries")
                .partitions(List.of(0))
                .partitionOffsets(new HashMap<>())
                .consumerProperties(consumerProperties)
                .build();

    }

    @Bean
    public MongoItemWriter<Country> countryMongoItemWriter() {
        return new MongoItemWriterBuilder<Country>()
                .template(mongoTemplate)
                .collection("country")
                .build();
    }
}
