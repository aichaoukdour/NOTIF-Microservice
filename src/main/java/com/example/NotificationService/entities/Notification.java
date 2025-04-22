package com.example.NotificationService.entities;


import java.time.LocalDate;
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
        private String content;
        private LocalDateTime sendDate;

        @Enumerated(EnumType.STRING)
        @Column(name = "status", nullable = false)
        private NotificationStatus status;

        @ManyToOne(cascade = CascadeType.ALL)
        private Template template;
    }
    

  
   


    

