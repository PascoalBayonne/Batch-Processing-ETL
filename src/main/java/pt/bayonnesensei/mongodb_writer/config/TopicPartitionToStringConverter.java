package pt.bayonnesensei.mongodb_writer.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.core.convert.converter.Converter;

public class TopicPartitionToStringConverter implements Converter<TopicPartition, String> {
    @Override
    public String convert(TopicPartition source) {
        return source.topic() + "-" + source.partition();
    }
}
