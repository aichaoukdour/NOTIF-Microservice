package com.example.NotificationService.dto;

import lombok.Data;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class TemplateRequest {

    @NotNull(message = "Name cannot be null")
    @NotEmpty(message = "Name cannot be empty")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Language cannot be null")
    @NotEmpty(message = "Language cannot be empty")
    private String language;

    @NotNull(message = "Template path cannot be null")
    @NotEmpty(message = "Template path cannot be empty")
    private String templatePath;

    @NotNull(message = "Variables list cannot be null")
    @Size(min = 1, message = "Variables list must contain at least one variable")
    private List<@NotEmpty(message = "Variable name cannot be empty") String> variables;
}
