package pt.bayonnesensei.mongodb_writer.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MongoJobExplorerFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MongoJobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

@Configuration
public class MongoDbConfig {

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create();
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        var mongoTemplate = new MongoTemplate(mongoClient, "country");
        var mappingMongoConverter = (MappingMongoConverter) mongoTemplate.getConverter();
        mappingMongoConverter.setMapKeyDotReplacement(".");
        mappingMongoConverter.setCustomConversions(customConversions());
        mappingMongoConverter.afterPropertiesSet();
        return mongoTemplate;
    }

    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTransactionManager(mongoDatabaseFactory);
    }

    public CustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList(new TopicPartitionToStringConverter(), new StringToTopicPartitionConverter()));
    }

    @Bean
    public JobRepository jobRepository(MongoTemplate mongoTemplate, MongoTransactionManager transactionManager) throws Exception {
        MongoJobRepositoryFactoryBean jobRepositoryFactoryBean = new MongoJobRepositoryFactoryBean();
        jobRepositoryFactoryBean.setMongoOperations(mongoTemplate);
        jobRepositoryFactoryBean.setTransactionManager(transactionManager);
        jobRepositoryFactoryBean.afterPropertiesSet();
        return jobRepositoryFactoryBean.getObject();
    }

    @Bean
    public JobExplorer jobExplorer(MongoTemplate mongoTemplate, MongoTransactionManager transactionManager) throws Exception {
        var mongoExplorerFactoryBean = new MongoJobExplorerFactoryBean();
        mongoExplorerFactoryBean.setMongoOperations(mongoTemplate);
        mongoExplorerFactoryBean.setTransactionManager(transactionManager);
        mongoExplorerFactoryBean.afterPropertiesSet();
        return mongoExplorerFactoryBean.getObject();
    }


}
