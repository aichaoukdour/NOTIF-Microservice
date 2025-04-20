package com.example.NotificationService.controllers;

import com.example.NotificationService.dto.NotificationRequest;
import com.example.NotificationService.entities.Notification;
import com.example.NotificationService.services.NotificationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> sendEmail(@RequestBody NotificationRequest request) {
        service.sendTemplatedEmail(
            request.getTo(),
            request.getSubject(),
            request.getTemplate(),
            request.getVariables()
        );
        return ResponseEntity.ok().build();
    }
}

