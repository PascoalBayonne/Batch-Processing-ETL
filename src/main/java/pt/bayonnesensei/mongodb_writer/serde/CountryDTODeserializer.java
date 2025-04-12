package pt.bayonnesensei.mongodb_writer.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import pt.bayonnesensei.mongodb_writer.dto.CountryDTO;

@Slf4j
public class CountryDTODeserializer implements Deserializer<CountryDTO> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public CountryDTO deserialize(String topic, byte[] data) {
        log.debug("Deserializing country DTO: {}", data);
        try {
            return (data == null) ? null : objectMapper.readValue(data, CountryDTO.class);
        } catch (Exception e) {
            log.error("Error deserializing country DTO: {}", e);
            throw new IllegalStateException(e);
        }

    }
}
