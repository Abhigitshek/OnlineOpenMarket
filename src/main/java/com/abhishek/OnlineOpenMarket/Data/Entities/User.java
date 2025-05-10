package com.abhishek.OnlineOpenMarket.Data.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String firstName;
    private String lastName;
    private Long membershipTierID;
    private Instant membershipStartedFrom;
    private Double walletAmount;
    private String address;
}
