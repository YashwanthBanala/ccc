package com.example.demo.repository;

import com.example.demo.entity.Plan;
import com.example.demo.entity.Customer.BillingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByActiveTrue();

    List<Plan> findByBillingType(BillingType billingType);

    Plan findByName(String name);
}
