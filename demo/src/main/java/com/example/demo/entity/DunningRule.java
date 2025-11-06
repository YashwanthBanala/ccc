package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dunning_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DunningRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Customer.BillingType billingType; // PREPAID / POSTPAID

    private int minOverdueDays;
    private int maxOverdueDays;

    private String action; // MESSAGE / THROTTLE / BAR
    private String messageTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = true)
    private Plan plan; // Optional plan-specific rule
}
