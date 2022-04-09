package pt.com.bayonnesensei.salesInfo.batch.dto;

import lombok.Data;

@Data
public class SalesInfoDTO {
    private String product;
    private String seller;
    private Integer sellerId;
    private double price;
    private String city;
    private String category;
}
