package pt.com.bayonnesensei.salesInfo.batch;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import pt.com.bayonnesensei.salesInfo.SalesInfoApplication;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

@SpringBatchTest
@SpringJUnitConfig({SalesInfoApplication.class, SalesInfoJobConfig.class})
@ActiveProfiles("test")
@TestPropertySource("classpath:application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SalesInfoJobConfigTest {

    private static final Path INPUT_DIRECTORY = Path.of("target/sales-info");
    private static final Path EXPECTED_COMPLETED_OUTPUT_DIRECTORY = Path.of("target/sales-info/processed");
    private static final Path EXPECTED_FAILED_DIRECTORY = Path.of("target/sales-info/failed");

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        if (Files.notExists(INPUT_DIRECTORY)) {
            Files.createDirectory(INPUT_DIRECTORY);
        }

    }

    @SneakyThrows
    @AfterEach
    public void cleanUp() {
        Files.walk(INPUT_DIRECTORY)
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(File::delete);

        jobRepositoryTestUtils.removeJobExecutions();
    }

    @SneakyThrows
    @Test
    void shouldReadFromFileAndPersistItemsIntoDatabase() {

        //given
        Path shouldCompleteFilePath = Files.createFile(Path.of(INPUT_DIRECTORY + File.separator + "sales-info-test-should-complete.csv"));
        Files.writeString(shouldCompleteFilePath, TestDataUtils.supplyValidContent());

        var jobParameters = new JobParametersBuilder()
                .addString("input.file.name", shouldCompleteFilePath.toString())
                .addDate("uniqueness", new Date())
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //then
        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());

        Integer totalRowsInserted = jdbcTemplate.queryForObject("select count(*) from sales_info", Integer.class);
        Assertions.assertEquals(4, totalRowsInserted);
        Assertions.assertTrue(Files.exists(EXPECTED_COMPLETED_OUTPUT_DIRECTORY));

        //check that files have been collected to correct dir
        boolean present = Files.list(EXPECTED_COMPLETED_OUTPUT_DIRECTORY)
                .findAny()
                .isPresent();
        Assertions.assertTrue(present);
    }


    @SneakyThrows
    @Test
    void shouldFailWhenInputFileContentIsInvalid() {

        //given
        Path filePathWithInvalidContent = Files.createFile(Path.of(INPUT_DIRECTORY + File.separator + "sales-info-test-should-fail.csv"));
        Files.writeString(filePathWithInvalidContent, TestDataUtils.supplyInvalidFileContent());

        var jobParameters = new JobParametersBuilder()
                .addString("input.file.name", filePathWithInvalidContent.toString())
                .addDate("uniqueness", new Date())
                .toJobParameters();

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //then
        boolean fromFileIntoDatabaseFailed = jobExecution.getStepExecutions()
                .stream()
                .filter(stepExecution -> "fromFileIntoDatabase".equalsIgnoreCase(stepExecution.getStepName()))
                .anyMatch(stepExecution -> ExitStatus.FAILED.getExitCode().equalsIgnoreCase(stepExecution.getExitStatus().getExitCode()));
        Assertions.assertTrue(fromFileIntoDatabaseFailed);
        Assertions.assertEquals(ExitStatus.FAILED, jobExecution.getExitStatus());

        Integer totalRowsInserted = jdbcTemplate.queryForObject("select count(*) from sales_info", Integer.class);
        Assertions.assertEquals(0, totalRowsInserted);
        Assertions.assertTrue(Files.exists(EXPECTED_FAILED_DIRECTORY));

        //check that files have been collected to correct dir
        boolean present = Files.list(EXPECTED_FAILED_DIRECTORY)
                .findAny()
                .isPresent();
        Assertions.assertTrue(present);
    }
}