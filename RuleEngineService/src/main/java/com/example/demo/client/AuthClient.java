package com.example.demo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "AUTH-SERVICE", path = "/auth")
public interface AuthClient {
    @GetMapping("/validate")
    Boolean validateToken(@RequestParam("token") String token);
}
