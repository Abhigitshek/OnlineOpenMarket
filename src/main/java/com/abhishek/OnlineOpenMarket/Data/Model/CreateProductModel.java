package com.abhishek.OnlineOpenMarket.Data.Model;

import lombok.Data;

@Data
public class CreateProductModel {
    private String name;
    private Double price;
    private Long subCategoryID;
    private String[] features;
}