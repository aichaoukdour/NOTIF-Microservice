package com.example.NotificationService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "failed_notification_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailedNotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;

    @Column(name = "error_message", nullable = false)
    private String errorMessage;

    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    public FailedNotificationLog(Notification notification, String errorMessage) {
        this.notification = notification;
        this.errorMessage = errorMessage;
        this.createdAt = LocalDateTime.now();
    }
}
