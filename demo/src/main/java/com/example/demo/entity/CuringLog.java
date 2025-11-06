package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "curing_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuringLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private String action; // e.g., "SERVICE_RESTORED"
    private LocalDateTime actionTime;
    private String remarks;
}
