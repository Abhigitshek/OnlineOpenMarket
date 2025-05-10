package com.abhishek.OnlineOpenMarket.Data.Model;

import lombok.Data;

@Data
public class CreateDeliveryRequestModel {
    private Long orderID;
    private Boolean oneDayDelivery;
    private String callbackURL;
    private String address;
}
