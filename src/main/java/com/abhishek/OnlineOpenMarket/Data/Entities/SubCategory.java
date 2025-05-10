package com.abhishek.OnlineOpenMarket.Data.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long subCategoryID;
    private String name;
    private Long categoryID;
    private String description;
}
