package com.abhishek.OnlineOpenMarket.Data.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MembershipTier {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tierID;
    private String name;
    private Double price;
    private boolean freeVideoService;
    private boolean oneDayDelivery;
    private boolean freeDelivery;
    private boolean fivePCCashback;
    public Double dailyPrice()
    {
        return price/365;
    }
}
