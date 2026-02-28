package pt.bayonnesensei.vehicles.dto;

public record VehicleDTO(Long id,
                         String name,
                         String brand,
                         String model,
                         Integer year,
                         String vin) {
}

