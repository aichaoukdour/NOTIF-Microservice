package com.example.NotificationService.controllers;

import com.example.NotificationService.dto.TemplateRequest;
import com.example.NotificationService.entities.Template;
import com.example.NotificationService.exception.TemplateNotFoundException;
import com.example.NotificationService.services.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping
    public ResponseEntity<Template> createTemplate(@RequestBody TemplateRequest templateRequest) {
        Template createdTemplate = templateService.saveTemplate(templateRequest);
        return ResponseEntity.ok(createdTemplate);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Template> getTemplateByName(@PathVariable String name) {
        return templateService.getTemplateByName(name)
                .map(this::sanitizeTemplate)
                .orElseThrow(() -> new TemplateNotFoundException(name));
    }

    private ResponseEntity<Template> sanitizeTemplate(Template template) {
        template.getVariables().forEach(variable -> variable.setId(null)); // Remove IDs from variables
        return ResponseEntity.ok(template);
    }
}
