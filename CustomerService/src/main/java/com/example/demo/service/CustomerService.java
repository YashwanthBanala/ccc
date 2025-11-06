package com.example.demo.service;

import com.example.demo.client.NotificationClient;
import com.example.demo.dto.NotificationRequest;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Plan;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PlanRepository planRepository;
    private final NotificationClient notificationClient; // ✅ Feign client for Notification service

    // ✅ Create new customer
    public Customer createCustomer(Customer customer) {
        if (customer.getPlan() != null && customer.getPlan().getId() != null) {
            Plan plan = planRepository.findById(customer.getPlan().getId())
                    .orElseThrow(() -> new RuntimeException("Plan not found with ID: " + customer.getPlan().getId()));
            customer.setPlan(plan);
        }

        Customer saved = customerRepository.save(customer);

        // ✅ Send welcome notification
        notificationClient.sendNotification(new NotificationRequest(
                saved.getId(),
                saved.getEmail(),
                saved.getPhoneNumber(),
                "🎉 Welcome " + saved.getName() + "! Your account has been successfully created."
        ));

        return saved;
    }

    // ✅ Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // ✅ Get customer by ID
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    // ✅ Update existing customer
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        return customerRepository.findById(id).map(existing -> {
            existing.setName(updatedCustomer.getName());
            existing.setEmail(updatedCustomer.getEmail());
            existing.setPhoneNumber(updatedCustomer.getPhoneNumber());
            existing.setStatus(updatedCustomer.getStatus());
            existing.setAddress(updatedCustomer.getAddress());

            if (updatedCustomer.getPlan() != null && updatedCustomer.getPlan().getId() != null) {
                Plan plan = planRepository.findById(updatedCustomer.getPlan().getId())
                        .orElseThrow(() -> new RuntimeException("Plan not found"));
                existing.setPlan(plan);
            }

            Customer updated = customerRepository.save(existing);

            // ✅ Send profile update notification
            notificationClient.sendNotification(new NotificationRequest(
                    updated.getId(),
                    updated.getEmail(),
                    updated.getPhoneNumber(),
                    "ℹ️ Hi " + updated.getName() + ", your profile has been updated successfully."
            ));

            return updated;
        }).orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
    }

    // ✅ Delete customer
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with ID: " + id);
        }

        Customer deleted = customerRepository.findById(id).orElseThrow();
        customerRepository.deleteById(id);

        // ✅ Send deletion notification
        notificationClient.sendNotification(new NotificationRequest(
                deleted.getId(),
                deleted.getEmail(),
                deleted.getPhoneNumber(),
                "⚠️ Dear " + deleted.getName() + ", your account has been deleted as per request."
        ));
    }

    // ✅ Get customer's assigned plan
    public Plan getCustomerPlan(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        if (customer.getPlan() == null) {
            throw new RuntimeException("No plan assigned to this customer");
        }
        return customer.getPlan();
    }

    // ✅ Assign a plan to customer
    public Plan assignPlanToCustomer(Long customerId, Long planId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        customer.setPlan(plan);
        customerRepository.save(customer);

        // ✅ Send plan assignment notification
        notificationClient.sendNotification(new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                "📦 Hi " + customer.getName() + ", your plan has been updated to: " + plan.getName() +
                        ". Monthly charge: ₹" + plan.getMonthlyCharge() +
                        " | Validity: " + plan.getValidityDays() + " days."
        ));

        return plan;
    }

    // ✅ Add a new plan
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
