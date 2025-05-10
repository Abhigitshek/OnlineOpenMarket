package com.abhishek.OnlineOpenMarket.Data.Model;

import com.google.gson.Gson;
import lombok.Data;

@Data
public class PayloadModel {
    private String eventType;
    private String payloadData;
    private Long orderID;

    @Override
    public String toString()
    {
        var gson = new Gson();
        return gson.toJson(this);
    }
}
