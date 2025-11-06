package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private Double amount;
    private String paymentMode; // CREDIT_CARD, UPI, CASH, etc.
    private String paymentStatus; // PAID, FAILED, PENDING
    private LocalDateTime paymentDate;

    private String billReference; // For linking with billing event
    private String remarks;
}
