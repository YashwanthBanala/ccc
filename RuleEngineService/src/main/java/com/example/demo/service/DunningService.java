package com.example.demo.service;

import com.example.demo.client.CustomerClient;
import com.example.demo.client.NotificationClient;
import com.example.demo.dto.NotificationRequest;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DunningService {

    private final DunningRuleRepository ruleRepo;
    private final DunningEventRepository eventRepo;
    private final CuringActionRepository curingRepo;

    private final CustomerClient customerClient;
    private final NotificationClient notificationClient;

    // ---------------- RULE CRUD ----------------
    public DunningRule createRule(DunningRule rule) {
        return ruleRepo.save(rule);
    }

    public List<DunningRule> getAllRules() {
        return ruleRepo.findAll();
    }

    public DunningRule getRuleById(Long id) {
        return ruleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Rule not found"));
    }

    public DunningRule updateRule(Long id, DunningRule updated) {
        DunningRule rule = getRuleById(id);
        rule.setRuleName(updated.getRuleName());
        rule.setOverdueDays(updated.getOverdueDays());
        rule.setAction(updated.getAction());
        rule.setDescription(updated.getDescription());
        return ruleRepo.save(rule);
    }

    public void deleteRule(Long id) {
        ruleRepo.deleteById(id);
    }

    // ---------------- EVENTS ----------------
    public DunningEvent recordEvent(DunningEvent event) {
        event.setEventTime(LocalDateTime.now());

        try {
            // 🧠 Fetch customer contact from Customer-Service
            Map<String, String> contact = customerClient.getCustomerContact(event.getCustomerId());
            String email = contact.get("email");
            String phone = contact.get("phone");
            String name = contact.get("name");

            String message = buildMessage(event.getEventType(), name);

            // 📨 Build NotificationRequest
            NotificationRequest notification = new NotificationRequest(
                    event.getCustomerId(),
                    email,
                    phone,
                    message
            );

            // 🚀 Send to Notification-Service
            notificationClient.sendNotification(notification);
            System.out.println("✅ Notification sent for event ID: " + event.getId());

        } catch (Exception e) {
            System.err.println("⚠️ Failed to send notification for event ID "
                    + event.getId() + ": " + e.getMessage());
        }

        return eventRepo.save(event);
    }

    private String buildMessage(String eventType, String customerName) {
        switch (eventType.toUpperCase()) {
            case "OVERDUE":
                return "Dear " + customerName + ", your payment is overdue. Please pay to avoid suspension.";
            case "SERVICE_STOPPED":
                return "Hi " + customerName + ", your service has been temporarily suspended due to non-payment.";
            case "PLAN_EXPIRING":
                return "Hi " + customerName + ", your plan will expire soon. Renew to continue service.";
            case "PLAN_EXPIRED":
                return "Dear " + customerName + ", your plan has expired. Please recharge to continue services.";
            case "CURED":
                return "Welcome back " + customerName + "! Your service has been reactivated successfully.";
            default:
                return "Notification from Telecom Dunning System regarding your account.";
        }
    }

    // ---------------- CURING ACTIONS ----------------
    public CuringAction triggerCuringAction(CuringAction action) {
        action.setActionTime(LocalDateTime.now());
        action.setActionStatus("PENDING");

        CuringAction saved = curingRepo.save(action);

        try {
            // 🧠 Fetch customer contact from Customer-Service
            Map<String, String> contact = customerClient.getCustomerContact(action.getCustomerId());
            String email = contact.get("email");
            String phone = contact.get("phone");

            // 📨 Build NotificationRequest
            NotificationRequest notification = new NotificationRequest(
                    action.getCustomerId(),
                    email,
                    phone,
                    "Your curing process has started. Service will resume after payment verification."
            );

            // 🚀 Send to Notification-Service
            notificationClient.sendNotification(notification);
            System.out.println("✅ Curing notification sent for customer ID: " + action.getCustomerId());

        } catch (Exception e) {
            System.err.println("⚠️ Failed to send curing notification for customer ID "
                    + action.getCustomerId() + ": " + e.getMessage());
        }

        return saved;
    }

    public List<DunningEvent> getAllEvents() {
        return eventRepo.findAll();
    }

    public List<DunningEvent> getEventsByCustomer(Long customerId) {
        List<DunningEvent> events = eventRepo.findByCustomerId(customerId);

        if (!events.isEmpty()) {
            try {
                // 🧠 Fetch customer contact from Customer-Service
                Map<String, String> contact = customerClient.getCustomerContact(customerId);
                String email = contact.get("email");
                String phone = contact.get("phone");
                String name = contact.get("name");

                // 📨 Build NotificationRequest
                NotificationRequest notification = new NotificationRequest(
                        customerId,
                        email,
                        phone,
                        "Hi " + name + ", here's a summary of your recent account events."
                );

                // 🚀 Send to Notification-Service
                notificationClient.sendNotification(notification);
                System.out.println("✅ Event summary notification sent for customer ID: " + customerId);

            } catch (Exception e) {
                System.err.println("⚠️ Failed to send event summary notification for customer ID "
                        + customerId + ": " + e.getMessage());
            }
        }

        return events;
    }

    public List<CuringAction> getAllCuringActions() {
        return curingRepo.findAll();
    }

    public List<CuringAction> getCuringActionsByCustomer(Long customerId) {
        return curingRepo.findByCustomerId(customerId);
    }
}