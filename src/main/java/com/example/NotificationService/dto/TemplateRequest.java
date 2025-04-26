package com.example.NotificationService.dto;

import lombok.Data;
import java.util.List;

@Data
public class TemplateRequest {
    private String name;
    private String language;
    private String templatePath;
    private List<String> variables; // une simple liste de noms de variables
}
