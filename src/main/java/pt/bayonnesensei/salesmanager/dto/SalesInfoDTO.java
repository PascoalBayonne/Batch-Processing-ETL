package pt.bayonnesensei.salesmanager.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class SalesInfoDTO implements Serializable {
    private String product;
    private String seller;
    private Integer sellerId;
    private double price;
    private String city;
    private String category;
}
