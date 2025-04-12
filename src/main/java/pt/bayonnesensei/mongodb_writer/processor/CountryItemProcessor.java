package pt.bayonnesensei.mongodb_writer.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import pt.bayonnesensei.mongodb_writer.domain.Country;
import pt.bayonnesensei.mongodb_writer.dto.CountryDTO;
import pt.bayonnesensei.mongodb_writer.processor.mapper.CountryMapper;

@Component
@Slf4j
public class CountryItemProcessor implements ItemProcessor<CountryDTO, Country> {

    @Override
    public Country process(CountryDTO item) throws Exception {
        log.info("-----------------------> Processing CountryDTO: {}", item);
        return CountryMapper.INSTANCE.mapToCountryDomain(item);
    }
}
