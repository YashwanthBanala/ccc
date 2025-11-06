package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private Double amount;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private BillStatus paidStatus; // PAID / UNPAID

    private LocalDate paymentDate;

    public enum BillStatus {
        PAID, UNPAID
    }
}
