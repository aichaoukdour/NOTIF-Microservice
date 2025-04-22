package com.example.NotificationService.dto;

import com.example.NotificationService.enums.NotificationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class NotificationResponse {
    private String recipient;
    private String message;
    private NotificationStatus status;
}

