package com.abhishek.OnlineOpenMarket.Data.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productID;
    private String name;
    private Double price;
    private Long subCategoryID;
    private String[] features;

}
