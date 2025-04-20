package com.example.NotificationService.exception;



import lombok.Getter;
import java.time.LocalDateTime;

/**
 * Classe de base pour toutes les exceptions métier de l'application
 */
@Getter
public abstract class BaseException extends RuntimeException {
    
    private final String errorCode;
    private final LocalDateTime timestamp;
    
    protected BaseException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }
    
    protected BaseException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }
}