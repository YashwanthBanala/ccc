package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@FeignClient(name = "customer-service")
public interface CustomerClient {
    @GetMapping("/customers/{id}/contact")
    Map<String, String> getCustomerContact(@PathVariable Long id);
}
