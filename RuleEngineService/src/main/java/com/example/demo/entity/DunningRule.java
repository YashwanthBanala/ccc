package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DunningRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruleName;
    private Integer overdueDays; // e.g. 5 days overdue
    private String action;       // e.g. "SEND_SMS", "SUSPEND_SERVICE"
    private String description;
}
