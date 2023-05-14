package pt.com.bayonnesensei.salesInfo.batch;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import pt.com.bayonnesensei.salesInfo.SalesInfoApplication;
import pt.com.bayonnesensei.salesInfo.batch.dto.SalesInfoDTO;
import pt.com.bayonnesensei.salesInfo.batch.integration.SalesInfoIntegrationConfig;
import pt.com.bayonnesensei.salesInfo.config.AbstractContainerProvider;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.DatabaseMetaData;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Stream;

@SpringBatchTest
@SpringJUnitConfig({SalesInfoApplication.class, SalesInfoJobConfig.class, SalesInfoIntegrationConfig.class})
@TestPropertySource("classpath:application-test.properties")
@Slf4j
class SalesInfoJobConfigTest extends AbstractContainerProvider {

    private static final Path INPUT_DIRECTORY = Path.of("target/sales-info");
    private static final Path EXPECTED_COMPLETED_DIRECTORY = Path.of("target/sales-info/processed");
    private static final Path EXPECTED_FAILED_DIRECTORY = Path.of("target/sales-info/failed");

    private KafkaItemReader<String, SalesInfoDTO> reader;

    private Properties consumerProperties;

    @Autowired
    private  Environment environment;


    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        if (Files.notExists(INPUT_DIRECTORY)) {
            Files.createDirectory(INPUT_DIRECTORY);
        }
        jobRepositoryTestUtils.removeJobExecutions();
        DataSource dataSource = jdbcTemplate.getDataSource();
        DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
        log.info("------------> we are using the database url: {}", metaData.getURL());

        //kafka
        String property = environment.getProperty("spring.kafka.bootstrap-servers");
        this.consumerProperties = new Properties();
        this.consumerProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                property);
        this.consumerProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "1");
        this.consumerProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class.getName());
        this.consumerProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class.getName());

    }


    @AfterEach
    @SneakyThrows
    void tearDown() {
        try (Stream<Path> filesTree = Files.walk(INPUT_DIRECTORY)) {
            filesTree.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .peek(fileToDelete -> log.warn("deleting the file: {}", fileToDelete.getName()))
                    .forEach(File::delete);
        }
    }


    @Test
    @DisplayName("GIVEN a directory with valid files WHEN jobLaunched THEN records persisted into DB and the input file is moved to processed directory")
    @SneakyThrows
    void shouldReadFromFileAndPersistIntoDataBaseAndMoveToProcessedDirectory() {
        //GIVEN
        Path shouldCompleteFilePath = Path.of(INPUT_DIRECTORY + File.separator + "sales-info-test-should-complete.csv");
        Path inputFile = Files.createFile(shouldCompleteFilePath);

        Files.writeString(inputFile, SalesInfoTestDataProviderUtils.supplyValidContent());

        //WHEN
        var jobParameters = new JobParametersBuilder()
                .addString("input.file.name", inputFile.toString())
                .addDate("uniqueness", new Date())
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //THEN
        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());

        Integer totalRowsInserted = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sales_info", Integer.class);
        Assertions.assertEquals(4, totalRowsInserted);
        Assertions.assertTrue(Files.exists(EXPECTED_COMPLETED_DIRECTORY));

        boolean containsAnyFile = Files.list(EXPECTED_COMPLETED_DIRECTORY)
                .findAny()
                .isPresent();

        Assertions.assertTrue(containsAnyFile);
    }

    @Test
    @DisplayName("GIVEN a directory with invalid files WHEN jobLaunched THEN exit status if FAILED and file is moved into failed directory")
    @SneakyThrows
    void shouldFailWhenInputFileContainsInvalidData() {
        //GIVEN
        Path shouldFailFilePath = Path.of(INPUT_DIRECTORY + File.separator + "sales-info-test-should-fail.csv");
        Path inputFile = Files.createFile(shouldFailFilePath);

        Files.writeString(inputFile, SalesInfoTestDataProviderUtils.supplyInvalidContent());

        //WHEN
        var jobParameters = new JobParametersBuilder()
                .addString("input.file.name", inputFile.toString())
                .addDate("uniqueness", new Date())
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //WHEN
        boolean fromFileToDatabaseFailed = jobExecution.getStepExecutions()
                .stream()
                .filter(stepExecution -> "fromFileIntoDatabase".equalsIgnoreCase(stepExecution.getStepName()))
                .anyMatch(stepExecution -> ExitStatus.FAILED.getExitCode().equalsIgnoreCase(stepExecution.getExitStatus().getExitCode()));

        Assertions.assertTrue(fromFileToDatabaseFailed);
        Assertions.assertEquals(ExitStatus.FAILED, jobExecution.getExitStatus());

        Integer totalRowsInserted = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sales_info", Integer.class);
        Assertions.assertEquals(0, totalRowsInserted);
        Assertions.assertTrue(Files.exists(EXPECTED_FAILED_DIRECTORY));

        boolean containsAnyFile = Files.list(EXPECTED_FAILED_DIRECTORY)
                .findAny()
                .isPresent();

        Assertions.assertTrue(containsAnyFile);

    }
}