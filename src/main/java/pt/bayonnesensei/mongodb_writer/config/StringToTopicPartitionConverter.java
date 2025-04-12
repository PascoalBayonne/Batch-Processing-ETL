package pt.bayonnesensei.mongodb_writer.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.core.convert.converter.Converter;

public class StringToTopicPartitionConverter implements Converter<String, TopicPartition> {
    @Override
    public TopicPartition convert(String source) {
        String[] parts = source.split("-");
        return new TopicPartition(parts[0], Integer.parseInt(parts[1]));
    }
}
