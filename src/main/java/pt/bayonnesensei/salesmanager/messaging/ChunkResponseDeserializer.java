package pt.bayonnesensei.salesmanager.messaging;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.batch.integration.chunk.ChunkResponse;
import org.springframework.util.SerializationUtils;

@Slf4j
public class ChunkResponseDeserializer implements Deserializer<ChunkResponse> {

    @Override
    public ChunkResponse deserialize(String s, byte[] bytes) {
        log.debug("-----------> deserializing chunk response");
        if (bytes == null) {
            return null;
        }
        return (ChunkResponse) SerializationUtils.deserialize(bytes);
    }
}
