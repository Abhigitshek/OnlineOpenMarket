package com.abhishek.OnlineOpenMarket.Data.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderID;

    private Long orderedByID;
    private Double orderAmount;
    private List<Long> productsIDs = new ArrayList<>();
    private Long paymentID;
    private String orderStatus;
    private String orderType;
    private String deliveryStatus;
    private Long deliveryID;
}
