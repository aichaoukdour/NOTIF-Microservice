package com.example.NotificationService.repositories;


import com.example.NotificationService.entities.FailedNotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedNotificationLogRepository extends JpaRepository<FailedNotificationLog, Long> {
}
