package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;               // e.g., "Platinum 999"
    private Double monthlyCharge;      // 999.00
    private Integer validityDays;      // e.g., 30 days
    private Double dataLimit;          // in GB
    private Integer voiceLimit;        // in minutes

    @Enumerated(EnumType.STRING)
    private Customer.BillingType billingType; // PREPAID / POSTPAID

    private boolean active = true;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<Customer> customers;
}
