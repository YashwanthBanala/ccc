package com.example.demo.controller;


import com.example.demo.client.AuthClient;
import com.example.demo.entity.*;
import com.example.demo.service.DunningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class DunningController {

    private final DunningService service;
    private final AuthClient authClient;

    private boolean isAuthorized(String token) {
        return token != null && token.startsWith("Bearer ")
                && authClient.validateToken(token.substring(7));
    }

    // ---------------- RULE CRUD ----------------
    @PostMapping("/rules")
    public ResponseEntity<?> createRule(@RequestHeader("Authorization") String token, @RequestBody DunningRule rule) {
        if (!isAuthorized(token)) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(service.createRule(rule));
    }

    @GetMapping("/rules")
    public ResponseEntity<?> getAllRules(@RequestHeader("Authorization") String token) {
        if (!isAuthorized(token)) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(service.getAllRules());
    }

    @GetMapping("/rules/{id}")
    public ResponseEntity<?> getRuleById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (!isAuthorized(token)) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(service.getRuleById(id));
    }

    @PutMapping("/rules/{id}")
    public ResponseEntity<?> updateRule(@RequestHeader("Authorization") String token,
                                        @PathVariable Long id, @RequestBody DunningRule rule) {
        if (!isAuthorized(token)) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(service.updateRule(id, rule));
    }

    @DeleteMapping("/rules/{id}")
    public ResponseEntity<?> deleteRule(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        if (!isAuthorized(token)) return ResponseEntity.status(401).body("Unauthorized");
        service.deleteRule(id);
        return ResponseEntity.ok("Rule deleted");
    }

    // ---------------- EVENTS ----------------
    @PostMapping("/events")
    public ResponseEntity<?> recordEvent(@RequestHeader("Authorization") String token,
                                         @RequestBody DunningEvent event) {
        if (!isAuthorized(token)) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(service.recordEvent(event));
    }

    @GetMapping("/events")
    public ResponseEntity<?> getAllEvents(@RequestHeader("Authorization") String token) {
        if (!isAuthorized(token)) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(service.getAllEvents());
    }

    @GetMapping("/events/customer/{customerId}")
    public ResponseEntity<?> getEventsByCustomer(@RequestHeader("Authorization") String token,
                                                 @PathVariable Long customerId) {
        if (!isAuthorized(token)) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(service.getEventsByCustomer(customerId));
    }

    // ---------------- CURING ----------------
    @PostMapping("/curing")
    public ResponseEntity<?> triggerCuring(@RequestHeader("Authorization") String token,
                                           @RequestBody CuringAction action) {
        if (!isAuthorized(token)) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(service.triggerCuringAction(action));
    }

    @GetMapping("/curing")
    public ResponseEntity<?> getAllCuringActions(@RequestHeader("Authorization") String token) {
        if (!isAuthorized(token)) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(service.getAllCuringActions());
    }

    @GetMapping("/curing/customer/{customerId}")
    public ResponseEntity<?> getCuringByCustomer(@RequestHeader("Authorization") String token,
                                                 @PathVariable Long customerId) {
        if (!isAuthorized(token)) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(service.getCuringActionsByCustomer(customerId));
    }
}
