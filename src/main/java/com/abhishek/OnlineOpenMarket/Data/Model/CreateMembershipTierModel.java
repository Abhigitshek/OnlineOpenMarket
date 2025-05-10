package com.abhishek.OnlineOpenMarket.Data.Model;

import lombok.Data;

@Data
public class CreateMembershipTierModel {
    private String name;
    private Double price;
    private boolean freeVideoService;
    private boolean oneDayDelivery;
    private boolean freeDelivery;
    private boolean fivePCCashback;
}
