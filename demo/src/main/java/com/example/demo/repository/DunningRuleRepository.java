package com.example.demo.repository;

import com.example.demo.entity.DunningRule;
import com.example.demo.entity.Customer.BillingType;
import com.example.demo.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DunningRuleRepository extends JpaRepository<DunningRule, Long> {

    List<DunningRule> findByBillingType(BillingType billingType);

    List<DunningRule> findByPlan(Plan plan);

    List<DunningRule> findByMinOverdueDaysLessThanEqualAndMaxOverdueDaysGreaterThanEqual(int min, int max);
}
