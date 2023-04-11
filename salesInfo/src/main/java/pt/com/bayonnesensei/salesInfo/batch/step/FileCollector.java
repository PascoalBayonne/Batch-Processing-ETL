package pt.com.bayonnesensei.salesInfo.batch.step;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

@Component
@Slf4j
public class FileCollector implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.warn("-------------> Executing the File Collector");
        Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();

        String inputFile = (String) jobParameters.get("input.file.name");

        String processedFolder = Path.of(inputFile).getParent() + File.separator + "processed";
        try (Stream<Path> filesToDelete = Files.walk(Path.of(processedFolder))) {
            filesToDelete.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        return RepeatStatus.FINISHED;
    }
}
