package pt.bayonnesensei.vehicles.job.common;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import pt.bayonnesensei.vehicles.dto.VehicleDTO;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class VehicleWriter {

    private final JdbcTemplate jdbcTemplate;

    public int[][] vehicleBatchItemWriter(final List<VehicleDTO> vehicleDTOS) {
        Objects.requireNonNull(vehicleDTOS, "vehicleDTOS cannot be null");
       return jdbcTemplate.batchUpdate(
                "INSERT INTO vehicles (code, brand, model, year, vin) " +
                        "VALUES (?, ?, ?, ?, ?)",
                vehicleDTOS,
                10, ((preparedStatement, vehicle) -> {
                    preparedStatement.setLong(1, vehicle.id());
                    preparedStatement.setString(2, vehicle.brand());
                    preparedStatement.setString(3, vehicle.model());
                    preparedStatement.setInt(4, vehicle.year());
                    preparedStatement.setString(5, vehicle.vin());
                })
        );
    }
}
