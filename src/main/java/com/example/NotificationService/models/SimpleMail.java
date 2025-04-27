package com.example.NotificationService.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleMail {
    private String to;
    private String subject;
    private String content;
}
