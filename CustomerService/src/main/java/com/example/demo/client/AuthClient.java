package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service", url = "http://localhost:1111")
public interface AuthClient {

    @GetMapping("/auth/validate")
    Boolean validateToken(@RequestParam("token") String token);
}
