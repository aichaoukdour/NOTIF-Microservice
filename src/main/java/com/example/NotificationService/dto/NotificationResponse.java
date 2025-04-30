package com.example.NotificationService.dto;

import com.example.NotificationService.enums.NotificationStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {
    private String recipientEmail;
    private String subject;
    private String content;
    private LocalDateTime sendDate;
    private NotificationStatus status;
    private String templateName;
}
