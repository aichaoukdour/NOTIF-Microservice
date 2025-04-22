package com.example.NotificationService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class NotificationRequest {

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email size must be less than 100 characters")
    @JsonProperty("to")
    private String email;

    @NotBlank(message = "Email subject is required.")
    private String subject;

    @NotBlank(message = "Template name is required.")
    private String template;

    private Map<String, Object> variables;

    private String content;
}
