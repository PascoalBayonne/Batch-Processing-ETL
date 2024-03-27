package pt.bayonnesensei.salesmanager.messaging;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.batch.integration.chunk.ChunkRequest;
import org.springframework.util.SerializationUtils;

@Slf4j
public class ChunkRequestSerializer implements Serializer<ChunkRequest> {

    @Override
    public byte[] serialize(String topic, ChunkRequest chunkRequest) {
        if (chunkRequest == null) {
            return new byte[0];
        }

        try {
            return SerializationUtils.serialize(chunkRequest);
        } catch (Exception e) {
            log.error("Error serializing data", e);
            throw new IllegalStateException(e);
        }

    }
}
