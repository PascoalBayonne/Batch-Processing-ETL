package pt.bayonnesensei.salesmanager.messaging;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.batch.integration.chunk.ChunkResponse;
import org.springframework.util.SerializationUtils;

@Slf4j
public class ChunkResponseSerializer implements Serializer<ChunkResponse> {

    @Override
    public byte[] serialize(String topic, ChunkResponse chunkResponse) {
        if (chunkResponse == null) {
            return null;
        }

        String dataType = chunkResponse.getClass().getName();
        log.debug("--> serializing: {}",dataType);

        byte[] dataBytes = null;
        try {


            dataBytes = SerializationUtils.serialize(chunkResponse);

        } catch (Exception e) {
            log.error("Error serializing data", e);
        }

        return dataBytes;
    }
}
