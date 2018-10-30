package pt.com.pascoal.easybatchsample.configuration;

import org.easybatch.core.filter.HeaderRecordFilter;
import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.writer.StandardOutputRecordWriter;
import org.easybatch.flatfile.FlatFileRecordReader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;


public class JobConfiguration {
    // Create the data source and jobName
    private static final String JOB_NAME = "import SOA Knights";
    private static final Path SOURCE_FILE = Paths.get("C:\\Users\\eddyb\\Desktop\\files\\waiting\\SOA knights.csv");
    private static final Logger logger = Logger.getLogger(JobConfiguration.class.getName());

    public static void main(String[] args) {
        Job simpleJob = JobBuilder
                .aNewJob()
                .named(JOB_NAME)
                .reader(new FlatFileRecordReader(SOURCE_FILE.toFile()))
                .filter(new HeaderRecordFilter())
                .writer(new StandardOutputRecordWriter())
                .batchSize(8)
                .build();

        JobExecutor jobExecutor = new JobExecutor();
        JobReport jobReport = jobExecutor.execute(simpleJob);
        jobExecutor.shutdown();

        logger.info(jobReport.toString());
    }

}
