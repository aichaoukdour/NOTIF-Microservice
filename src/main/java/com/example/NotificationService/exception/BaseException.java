package com.example.NotificationService.exception;

/**
 * Base exception class for custom exceptions.
 */
public class BaseException extends RuntimeException {

    private final String errorCode;

    public BaseException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
