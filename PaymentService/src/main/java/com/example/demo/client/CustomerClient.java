package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "CUSTOMER-SERVICE", path = "/customers")
public interface CustomerClient {

    // Endpoint in CustomerService: GET /customers/{id}/contact
    @GetMapping("/{id}/contact")
    Map<String, String> getCustomerContact(@PathVariable("id") Long customerId);
}
