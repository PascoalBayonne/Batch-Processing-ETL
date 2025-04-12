package pt.bayonnesensei.mongodb_writer.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.bayonnesensei.mongodb_writer.domain.Country;
import pt.bayonnesensei.mongodb_writer.dto.CountryDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CountryItemProcessorTest {

    private CountryItemProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new CountryItemProcessor();
    }

    @Test
    void testProcess() throws Exception {
        // Arrange
        CountryDTO countryDTO = new CountryDTO("PT","Portugal","Europe", 890, 3);

        // Act
        Country country = processor.process(countryDTO);

        // Assert
        assertNotNull(country, "The processed Country should not be null");
        assertEquals("Portugal", country.getCountryName(), "The name should match");
        assertEquals("PT", country.getCountryCode(), "The code should match");
    }
}
