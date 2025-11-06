package com.example.demo.service;


import com.example.demo.config.TwilioConfig;
import com.example.demo.dto.NotificationRequest;
import com.example.demo.entity.Notification;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.service.NotificationService;
import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final JavaMailSender mailSender;
    private final TwilioConfig twilioConfig;

    @Override
    public void sendNotification(NotificationRequest request) {
        String email = request.getEmail();
        String phone = request.getPhoneNumber();

        try {
            // ✅ Send Email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("Notification Alert");
            mailMessage.setText(request.getMessage());
            mailSender.send(mailMessage);

            // ✅ Send SMS
            Message.creator(
                    new com.twilio.type.PhoneNumber(phone),
                    new com.twilio.type.PhoneNumber(twilioConfig.getFromPhoneNumber()),
                    request.getMessage()
            ).create();

            // ✅ Save success
            repository.save(Notification.builder()
                    .customerId(request.getCustomerId())
                    .type("EMAIL+SMS")
                    .message(request.getMessage())
                    .status("SENT")
                    .sentAt(LocalDateTime.now())
                    .build());

        } catch (Exception e) {
            // ❌ Save failure
            repository.save(Notification.builder()
                    .customerId(request.getCustomerId())
                    .type("EMAIL+SMS")
                    .message(request.getMessage())
                    .status("FAILED")
                    .sentAt(LocalDateTime.now())
                    .build());
        }
    }
}
