package com.example.NotificationService.exception;

/**
 * Base exception class for custom exceptions.
 */
public class BaseException extends RuntimeException {

    private final String errorCode;

    /**
     * Constructs a new BaseException with the specified detail message and error code.
     *
     * @param message   the detail message
     * @param errorCode the error code associated with the exception
     */
    public BaseException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code associated with this exception.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}
