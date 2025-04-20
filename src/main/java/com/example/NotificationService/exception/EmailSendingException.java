package com.example.NotificationService.exception;

public class EmailSendingException extends BaseException {
    
    private static final String ERROR_CODE = "EMAIL_SENDING_FAILED";
    
    public EmailSendingException(String message) {
        super(message, ERROR_CODE);
    }
    
    public EmailSendingException(String message, Throwable cause) {
        super(message, ERROR_CODE, cause);
    }
}