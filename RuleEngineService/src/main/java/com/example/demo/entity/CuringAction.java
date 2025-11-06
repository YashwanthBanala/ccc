package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuringAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;
    private String actionType; // e.g. "SMS", "EMAIL", "CALL"
    private String actionStatus; // e.g. "PENDING", "COMPLETED"
    private LocalDateTime actionTime;
    private String remarks;
}
