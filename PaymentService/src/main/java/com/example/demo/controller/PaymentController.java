package com.example.demo.controller;

import com.example.demo.client.AuthClient;
import com.example.demo.entity.Payment;
import com.example.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;
    private final AuthClient authClient;

    private boolean isAuthorized(String token) {
        return token != null && token.startsWith("Bearer ")
                && authClient.validateToken(token.substring(7));
    }

    // ---------------- CREATE PAYMENT ----------------
    @PostMapping
    public ResponseEntity<?> createPayment(@RequestHeader("Authorization") String token,
                                           @RequestBody Payment payment) {
        if (!isAuthorized(token)) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(service.createPayment(payment));
    }

    // ---------------- READ ALL PAYMENTS ----------------
    @GetMapping
    public ResponseEntity<?> getAllPayments(@RequestHeader("Authorization") String token) {
        if (!isAuthorized(token)) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(service.getAllPayments());
    }

    // ---------------- READ PAYMENT BY ID ----------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentById(@RequestHeader("Authorization") String token,
                                            @PathVariable Long id) {
        if (!isAuthorized(token)) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(service.getPaymentById(id));
    }

    // ---------------- READ PAYMENTS BY CUSTOMER ----------------
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getPaymentsByCustomer(@RequestHeader("Authorization") String token,
                                                   @PathVariable Long customerId) {
        if (!isAuthorized(token)) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(service.getPaymentsByCustomerId(customerId));
    }

    // ---------------- UPDATE PAYMENT ----------------
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePayment(@RequestHeader("Authorization") String token,
                                           @PathVariable Long id,
                                           @RequestBody Payment payment) {
        if (!isAuthorized(token)) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(service.updatePayment(id, payment));
    }

    // ---------------- DELETE PAYMENT ----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePayment(@RequestHeader("Authorization") String token,
                                                @PathVariable Long id) {
        if (!isAuthorized(token)) return ResponseEntity.status(401).body("Unauthorized");
        service.deletePayment(id);
        return ResponseEntity.ok("Payment record deleted successfully");
    }
}
