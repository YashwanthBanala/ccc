package com.example.demo.controller;

import com.example.demo.client.AuthClient;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Plan;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.CustomerService;
import com.example.demo.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final PlanService planService;
    private final AuthClient authClient;
    private final CustomerRepository customerRepository;

    // ✅ Helper: validate JWT using Auth-Service
    private boolean isValid(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return false;
        String token = authHeader.substring(7);
        try {
            return Boolean.TRUE.equals(authClient.validateToken(token));
        } catch (Exception e) {
            System.out.println("❌ Token validation failed: " + e.getMessage());
            return false;
        }
    }
    @GetMapping("/{customerId}/contact")
    public Map<String, String> getCustomerContact(@PathVariable Long customerId) {
        Customer c = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return Map.of("email", c.getEmail(), "phone", c.getPhoneNumber());
    }

    // -------------------- 🧍 CUSTOMER CRUD --------------------

    @PostMapping
    public ResponseEntity<?> addCustomer(
            @RequestBody Customer customer,
            @RequestHeader("Authorization") String authHeader) {

        if (!isValid(authHeader))
            return ResponseEntity.status(401).body("Invalid or expired token");

        // ✅ Create customer + Feign triggers notification
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    @GetMapping
    public ResponseEntity<?> getAllCustomers(@RequestHeader("Authorization") String authHeader) {
        if (!isValid(authHeader))
            return ResponseEntity.status(401).body("Invalid token");

        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        if (!isValid(authHeader))
            return ResponseEntity.status(401).body("Invalid token");

        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(
            @PathVariable Long id,
            @RequestBody Customer customer,
            @RequestHeader("Authorization") String authHeader) {

        if (!isValid(authHeader))
            return ResponseEntity.status(401).body("Invalid token");

        // ✅ Updates customer + sends notification via Feign
        return ResponseEntity.ok(customerService.updateCustomer(id, customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        if (!isValid(authHeader))
            return ResponseEntity.status(401).body("Invalid token");

        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Customer deleted successfully");
    }

    // -------------------- 🔗 CUSTOMER-PLAN LINKING --------------------

    @PutMapping("/{customerId}/plan/{planId}")
    public ResponseEntity<?> assignPlanToCustomer(
            @PathVariable Long customerId,
            @PathVariable Long planId,
            @RequestHeader("Authorization") String authHeader) {

        if (!isValid(authHeader))
            return ResponseEntity.status(401).body("Invalid token");

        // ✅ Assigns plan + triggers notification
        Plan assignedPlan = customerService.assignPlanToCustomer(customerId, planId);
        return ResponseEntity.ok(assignedPlan);
    }

    @GetMapping("/{customerId}/plan")
    public ResponseEntity<?> getCustomerPlan(
            @PathVariable Long customerId,
            @RequestHeader("Authorization") String authHeader) {

        if (!isValid(authHeader))
            return ResponseEntity.status(401).body("Invalid token");

        return ResponseEntity.ok(customerService.getCustomerPlan(customerId));
    }

    // -------------------- 📦 PLAN CRUD (Admin APIs) --------------------

    @PostMapping("/plans")
    public ResponseEntity<?> addPlan(
            @RequestBody Plan plan,
            @RequestHeader("Authorization") String authHeader) {

        if (!isValid(authHeader))
            return ResponseEntity.status(401).body("Invalid or expired token");

        return ResponseEntity.ok(planService.addPlan(plan));
    }

    @GetMapping("/plans")
    public ResponseEntity<?> getAllPlans(@RequestHeader("Authorization") String authHeader) {
        if (!isValid(authHeader))
            return ResponseEntity.status(401).body("Invalid token");

        List<Plan> plans = planService.getAllPlans();
        return ResponseEntity.ok(plans);
    }

    @GetMapping("/plans/{id}")
    public ResponseEntity<?> getPlanById(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        if (!isValid(authHeader))
            return ResponseEntity.status(401).body("Invalid token");

        return planService.getPlanById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/plans/{id}")
    public ResponseEntity<?> updatePlan(
            @PathVariable Long id,
            @RequestBody Plan plan,
            @RequestHeader("Authorization") String authHeader) {

        if (!isValid(authHeader))
            return ResponseEntity.status(401).body("Invalid token");

        return ResponseEntity.ok(planService.updatePlan(id, plan));
    }

    @DeleteMapping("/plans/{id}")
    public ResponseEntity<?> deletePlan(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        if (!isValid(authHeader))
            return ResponseEntity.status(401).body("Invalid token");

        planService.deletePlan(id);
        return ResponseEntity.ok("Plan deleted successfully");
    }

}
