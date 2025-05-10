package com.abhishek.OnlineOpenMarket.Data.Model;

import lombok.Data;

@Data
public class CreateUserModel {
    private String firstName;
    private String lastName;
    private Long membershipTierID;
    private String address;
}
