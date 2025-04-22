package com.example.NotificationService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for custom exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles TemplateNotFoundException.
     *
     * @param ex the exception
     * @return ResponseEntity with error message and HTTP status
     */
    @ExceptionHandler(TemplateNotFoundException.class)
    public ResponseEntity<String> handleTemplateNotFoundException(TemplateNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    /**
     * Handles EmailSendingException.
     *
     * @param ex the exception
     * @return ResponseEntity with error message and HTTP status
     */
    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<String> handleEmailSendingException(EmailSendingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
