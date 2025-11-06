package com.example.demo.service;

import com.example.demo.client.CustomerClient;
import com.example.demo.client.NotificationClient;
import com.example.demo.dto.NotificationRequest;
import com.example.demo.entity.Payment;
import com.example.demo.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CustomerClient customerClient;      // ✅ Fetch contact details
    private final NotificationClient notificationClient; // ✅ Send notification

    // ---------------- CREATE PAYMENT ----------------
    public Payment createPayment(Payment payment) {
        payment.setPaymentDate(LocalDateTime.now());
        if (payment.getPaymentStatus() == null) {
            payment.setPaymentStatus("PENDING");
        }

        Payment savedPayment = paymentRepository.save(payment);

        // ✅ Notify customer if payment succeeded
        if ("SUCCESS".equalsIgnoreCase(savedPayment.getPaymentStatus())) {
            sendPaymentSuccessNotification(savedPayment);
        }

        return savedPayment;
    }

    // ---------------- Helper Method ----------------
    private void sendPaymentSuccessNotification(Payment payment) {
        try {
            // 🧠 Fetch customer contact from Customer-Service
            Map<String, String> contact = customerClient.getCustomerContact(payment.getCustomerId());
            String email = contact.get("email");
            String phone = contact.get("phone");

            // 📨 Build NotificationRequest
            NotificationRequest notification = new NotificationRequest(
                    payment.getCustomerId(),
                    email,
                    phone,
                    "Dear Customer, your payment of ₹" + payment.getAmount() +
                            " has been successfully processed. Thank you!"
            );

            // 🚀 Send to Notification-Service
            notificationClient.sendNotification(notification);
            System.out.println("✅ Notification sent for payment ID: " + payment.getId());

        } catch (Exception e) {
            System.err.println("⚠️ Failed to send notification for payment ID "
                    + payment.getId() + ": " + e.getMessage());
        }
    }

    // ---------------- READ PAYMENTS ----------------
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public List<Payment> getPaymentsByCustomerId(Long customerId) {
        return paymentRepository.findByCustomerId(customerId);
    }

    // ---------------- UPDATE PAYMENT ----------------
    public Payment updatePayment(Long id, Payment updated) {
        Payment existing = getPaymentById(id);
        existing.setAmount(updated.getAmount());
        existing.setPaymentMode(updated.getPaymentMode());
        existing.setPaymentStatus(updated.getPaymentStatus());
        existing.setRemarks(updated.getRemarks());

        Payment updatedPayment = paymentRepository.save(existing);

        // ✅ Trigger notification again if status changes to SUCCESS
        if ("SUCCESS".equalsIgnoreCase(updatedPayment.getPaymentStatus())) {
            sendPaymentSuccessNotification(updatedPayment);
        }

        return updatedPayment;
    }

    // ---------------- DELETE PAYMENT ----------------
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}
