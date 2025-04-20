package com.example.NotificationService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.NotificationService.entities.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> { }





