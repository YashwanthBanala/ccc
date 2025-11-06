package com.example.demo.service;

import com.example.demo.entity.Plan;
import com.example.demo.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    // ✅ Create or Add new plan
    public Plan addPlan(Plan plan) {
        return planRepository.save(plan);
    }

    // ✅ Get all plans
    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    // ✅ Get plan by ID
    public Optional<Plan> getPlanById(Long id) {
        return planRepository.findById(id);
    }

    // ✅ Update plan
    public Plan updatePlan(Long id, Plan updatedPlan) {
        return planRepository.findById(id)
                .map(existing -> {
                    existing.setName(updatedPlan.getName());
                    existing.setMonthlyCharge(updatedPlan.getMonthlyCharge());
                    existing.setValidityDays(updatedPlan.getValidityDays());
                    existing.setBillingType(updatedPlan.getBillingType());
                    return planRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Plan not found with ID: " + id));
    }

    // ✅ Delete plan
    public void deletePlan(Long id) {
        if (!planRepository.existsById(id)) {
            throw new RuntimeException("Plan not found with ID: " + id);
        }
        planRepository.deleteById(id);
    }
}
