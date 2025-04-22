package com.example.NotificationService.controllers;

import com.example.NotificationService.dto.NotificationRequest;
import com.example.NotificationService.dto.NotificationResponse;
import com.example.NotificationService.enums.NotificationStatus;
import com.example.NotificationService.services.NotificationService;

import lombok.extern.slf4j.Slf4j;

import com.example.NotificationService.exception.EmailSendingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/notifications")
@Slf4j
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponse> sendEmail(@RequestBody NotificationRequest request) {
        try {
            NotificationResponse response = notificationService.sendTemplatedEmail(request);
            return ResponseEntity.ok(response);
        } catch (EmailSendingException e) {
            log.error("Error sending email: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new NotificationResponse(request.getName(), "Failed to send email: " + e.getMessage(),
                            NotificationStatus.FAILED));
        }
    }
}
