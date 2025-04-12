package pt.bayonnesensei.mongodb_writer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Country {
    @Id
    private String id;
    private String countryName;
    private String continent;
    private Integer independenceYear;
    private Integer score;
    private String countryCode;
}
