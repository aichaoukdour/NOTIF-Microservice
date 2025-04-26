package com.example.NotificationService.services;

import com.example.NotificationService.entities.Template;
import com.example.NotificationService.repositories.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemplateService {

    private final TemplateRepository templateRepository;

    public Optional<Template> getTemplateById(Long id) {
        return templateRepository.findById(id);
    }
}
