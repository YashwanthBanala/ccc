package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phoneNumber;


    private String status; // ACTIVE / THROTTLED / BARRED
    private String address;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;
}
