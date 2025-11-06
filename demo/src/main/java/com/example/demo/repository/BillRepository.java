package com.example.demo.repository;

import com.example.demo.entity.Bill;
import com.example.demo.entity.Bill.BillStatus;
import com.example.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    List<Bill> findByPaidStatus(BillStatus status);

    List<Bill> findByCustomer(Customer customer);

    @Query("SELECT b FROM Bill b WHERE b.paidStatus = 'UNPAID' AND b.dueDate < :currentDate")
    List<Bill> findOverdueBills(LocalDate currentDate);

    @Query("SELECT b FROM Bill b WHERE b.customer.id = :customerId AND b.paidStatus = 'UNPAID'")
    List<Bill> findUnpaidBillsByCustomer(Long customerId);
}
