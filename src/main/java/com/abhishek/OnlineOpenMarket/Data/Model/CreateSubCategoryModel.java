package com.abhishek.OnlineOpenMarket.Data.Model;

import lombok.Data;

@Data
public class CreateSubCategoryModel {
    private String name;
    private String description;
    private Long categoryID;
}