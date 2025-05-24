package com.example.NotificationService.entities;



import java.time.LocalDateTime;

import com.example.NotificationService.enums.NotificationStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recipientEmail;
    private String subject;
    @Column(length = 1000) // Match database schema if using VARCHAR(1000)
    @Size(max = 1000, message = "Content must not exceed 1000 characters")
    private String content;
    private LocalDateTime sendDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;

    @ManyToOne(cascade = CascadeType.ALL)
    private Template template;
}

  
   


    

