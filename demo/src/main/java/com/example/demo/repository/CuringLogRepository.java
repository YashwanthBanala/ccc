package com.example.demo.repository;

import com.example.demo.entity.CuringLog;
import com.example.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CuringLogRepository extends JpaRepository<CuringLog, Long> {

    List<CuringLog> findByCustomer(Customer customer);
}
