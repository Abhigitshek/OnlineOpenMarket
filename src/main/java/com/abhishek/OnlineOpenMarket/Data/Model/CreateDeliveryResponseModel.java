package com.abhishek.OnlineOpenMarket.Data.Model;

import lombok.Data;

@Data
public class CreateDeliveryResponseModel {
    private Long deliveryID;
    private String status;
    private String message;
}
