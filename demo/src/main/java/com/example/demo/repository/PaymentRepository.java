package com.example.demo.repository;

import com.example.demo.entity.Payment;
import com.example.demo.entity.Payment.PaymentStatus;
import com.example.demo.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByBill(Bill bill);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByStatusAndBill_Customer_Id(PaymentStatus status, Long customerId);
}
