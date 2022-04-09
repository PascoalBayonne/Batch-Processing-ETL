package pt.com.bayonnesensei.salesInfo.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class SalesInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String product;
    private String seller;
    private Integer sellerId;
    private double price;
    private String city;
    private String category;
}
