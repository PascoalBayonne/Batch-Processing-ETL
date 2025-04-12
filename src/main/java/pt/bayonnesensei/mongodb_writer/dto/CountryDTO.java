package pt.bayonnesensei.mongodb_writer.dto;

public record CountryDTO(String countryCode,
                         String countryName,
                         String continent,
                         Integer independenceYear,
                         Integer score) {
}
