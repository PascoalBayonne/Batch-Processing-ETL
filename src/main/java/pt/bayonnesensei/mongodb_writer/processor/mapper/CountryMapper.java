package pt.bayonnesensei.mongodb_writer.processor.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pt.bayonnesensei.mongodb_writer.domain.Country;
import pt.bayonnesensei.mongodb_writer.dto.CountryDTO;

@Mapper
public interface CountryMapper {

    CountryMapper INSTANCE = Mappers.getMapper(CountryMapper.class);

    Country mapToCountryDomain(CountryDTO country);
}
