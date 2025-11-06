package com.example.demo.service;

import com.example.demo.dto.NotificationRequest;

public interface NotificationService {
    void sendNotification(NotificationRequest request);
}