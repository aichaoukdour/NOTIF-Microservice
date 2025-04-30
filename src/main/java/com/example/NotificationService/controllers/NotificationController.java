package com.example.NotificationService.controllers;

import com.example.NotificationService.dto.NotificationRequest;
import com.example.NotificationService.dto.NotificationResponse;
import com.example.NotificationService.entities.Notification;
import com.example.NotificationService.enums.NotificationStatus;
import com.example.NotificationService.services.NotificationService;

import lombok.extern.slf4j.Slf4j;

import com.example.NotificationService.exception.EmailSendingException;
import com.example.NotificationService.mapper.NotificationMapper;
import com.example.NotificationService.repositories.NotificationRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/notifications")
@Slf4j
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;
    

    @PostMapping
    public ResponseEntity<NotificationResponse> sendEmail(@RequestBody NotificationRequest request) {
        try {
            NotificationResponse response = notificationService.sendTemplatedEmail(request);
            return ResponseEntity.ok(response);
        } catch (EmailSendingException e) {
            log.error("Error sending email: {}", e.getMessage());
            // Return a proper internal server error response with details of the failure
            return ResponseEntity.internalServerError()
                    .body(NotificationResponse.builder()
                            .recipientEmail(request.getEmail())
                            .subject("Failed to send email: " + e.getMessage())
                            .content("Please check the email format or template name.")
                            .status(NotificationStatus.FAILED)
                            .templateName(null) // Set to null or any appropriate value
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    

    @GetMapping("/history/{email}")
public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
    List<Notification> notifications = notificationRepository.findAll();
    List<NotificationResponse> responses = notifications.stream()
            .map(notificationMapper::toResponse)
            .toList();
    return ResponseEntity.ok(responses);
}

    
}
