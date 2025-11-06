package com.example.demo.repository;


import com.example.demo.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DunningEventRepository extends JpaRepository<DunningEvent, Long> {
    List<DunningEvent> findByCustomerId(Long customerId);
}