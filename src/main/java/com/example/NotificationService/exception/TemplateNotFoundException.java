package com.example.NotificationService.exception;

/**
 * Exception thrown when a template is not found.
 */
public class TemplateNotFoundException extends BaseException {

    private static final String ERROR_CODE = "TEMPLATE_NOT_FOUND";

    public TemplateNotFoundException(String templateName) {
        super(String.format("Template '%s' not found", templateName), ERROR_CODE);
    }
}
