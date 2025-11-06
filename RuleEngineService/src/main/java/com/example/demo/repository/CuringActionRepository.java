package com.example.demo.repository;


import com.example.demo.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CuringActionRepository extends JpaRepository<CuringAction, Long> {
    List<CuringAction> findByCustomerId(Long customerId);
}