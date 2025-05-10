package com.abhishek.OnlineOpenMarket.Data.Model;

import com.google.gson.Gson;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PaymentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long paymentID;
    private Double amount;
    private Long orderID;
    private String callbackURL;
    private Boolean status;

    @Override
    public String toString()
    {
        var gson = new Gson();
        return gson.toJson(this);
    }
}