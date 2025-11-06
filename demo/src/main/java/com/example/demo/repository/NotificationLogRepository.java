package com.example.demo.repository;

import com.example.demo.entity.NotificationLog;
import com.example.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {

    List<NotificationLog> findByCustomer(Customer customer);

    List<NotificationLog> findByStatus(String status);

    List<NotificationLog> findByChannel(String channel);
}
