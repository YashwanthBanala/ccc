package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;            // e.g. "Platinum 999"
    private Double monthlyCharge;
    private Integer validityDays;
    private String description;
    @Enumerated(EnumType.STRING)
    private BillingType billingType; // PREPAID / POSTPAID
}
