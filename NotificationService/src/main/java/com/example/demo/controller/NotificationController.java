package com.example.demo.controller;

import com.example.demo.dto.NotificationRequest;
import com.example.demo.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public String send(@RequestBody NotificationRequest request) {
        notificationService.sendNotification(request);
        return "✅ Notification triggered successfully!";
    }
}
