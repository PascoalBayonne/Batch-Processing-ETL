package pt.bayonnesensei.salesmanager.messaging;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.batch.integration.chunk.ChunkRequest;
import org.springframework.util.SerializationUtils;

@Slf4j
public class ChunkRequestDeserializer implements Deserializer<ChunkRequest> {
    @Override
    public ChunkRequest deserialize(String s, byte[] bytes) {
        log.debug("-----------> deserializing chunk request");
        if (bytes == null) {
            return null;
        }
        return (ChunkRequest) SerializationUtils.deserialize(bytes);
    }
}
