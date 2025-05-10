package com.abhishek.OnlineOpenMarket.Data.Model;

import lombok.Data;

@Data
public class PaymentRequest {
    private Double amount;
    private Long orderID;
    private String callbackURL;
}
