package com.example.NotificationService.dto;

import com.example.NotificationService.enums.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class NotificationResponse {
    public NotificationResponse(String email, String plainText, NotificationStatus status2, Object object,
            String name) {
        //TODO Auto-generated constructor stub
    }
    private String recipientEmail;
    private String subject;
    private String content;
    private LocalDateTime sendDate;
    private NotificationStatus status;
    private String templateName;

}
