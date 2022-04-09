package pt.com.bayonnesensei.salesInfo.batch;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import pt.com.bayonnesensei.salesInfo.batch.dto.SalesInfoDTO;
import pt.com.bayonnesensei.salesInfo.batch.processor.SalesInfoItemProcessor;
import pt.com.bayonnesensei.salesInfo.domain.SalesInfo;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class SalesInfoJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final SalesInfoItemProcessor salesInfoItemProcessor;


    @Bean
    public Job importSalesInfo(){
        return jobBuilderFactory.get("importSalesInfo")
                .incrementer(new RunIdIncrementer())
                .start(fromFileIntoDataBase())
                .build();
    }


    @Bean
    public Step fromFileIntoDataBase(){
        return stepBuilderFactory.get("fromFileIntoDatabase")
                .<SalesInfoDTO,SalesInfo>chunk(10)
                .reader(salesInfoFileReader())
                .processor(salesInfoItemProcessor)
                .writer(salesInfoItemWriter())
                .build();
    }

    @Bean
    public FlatFileItemReader<SalesInfoDTO> salesInfoFileReader(){
        return new FlatFileItemReaderBuilder<SalesInfoDTO>()
                .resource(new ClassPathResource("data/Pascoal-Store.csv"))
                .name("salesInfoFileReader")
                .delimited()
                .delimiter(",")
                .names(new String[]{"product","seller","sellerId","price","city","category"})
                .linesToSkip(1)
                .targetType(SalesInfoDTO.class)
                .build();
    }

    @Bean
    public JpaItemWriter<SalesInfo> salesInfoItemWriter(){
        return new JpaItemWriterBuilder<SalesInfo>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

}
