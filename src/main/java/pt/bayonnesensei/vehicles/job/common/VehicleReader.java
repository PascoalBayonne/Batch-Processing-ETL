package pt.bayonnesensei.vehicles.job.common;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import pt.bayonnesensei.vehicles.dto.VehicleDTO;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class VehicleReader {

    private final RestClient restClient;

    public List<VehicleDTO> fetchVehicles(final String brandURI) {
        Objects.requireNonNull(brandURI, "brandURI must not be null");
        return restClient.get()
                .uri(brandURI)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
