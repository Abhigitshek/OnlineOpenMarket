package com.abhishek.OnlineOpenMarket.Data.Model;

import lombok.Data;

@Data
public class CreateOrderModel {
    private Long userID;
    private Long[] productIDs;
    private String orderType;
    private Double amount;
}
