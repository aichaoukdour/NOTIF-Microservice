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
    public Template createTemplate(@RequestBody TemplateRequest templateDTO) {
        return templateService.saveTemplate(templateDTO);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Template> getTemplateByName(@PathVariable String name) {
        return templateService.getTemplateByName(name)
                .map(template -> {
                    // Optionally, clean up variables by removing IDs if necessary
                    template.getVariables().forEach(var -> var.setId(null)); // Set IDs to null if you don’t want to return them

                    // Return the template with variables in the desired format
                    return ResponseEntity.ok(template);
                })
                .orElseThrow(() -> new TemplateNotFoundException(name)); // Throw custom excepti
            }}