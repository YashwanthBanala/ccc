package com.example.demo.repository;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Customer.BillingType;
import com.example.demo.entity.Customer.Status;
import com.example.demo.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByStatus(Status status);

    List<Customer> findByBillingType(BillingType billingType);

    List<Customer> findByPlan(Plan plan);

    List<Customer> findByStatusAndBillingType(Status status, BillingType billingType);
}
