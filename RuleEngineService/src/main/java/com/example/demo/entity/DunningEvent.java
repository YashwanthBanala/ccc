package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DunningEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private Long paymentId;
    private String eventType; // e.g. "OVERDUE_PAYMENT", "REMINDER_SENT"
    private LocalDateTime eventTime;
    private String remarks;
}
