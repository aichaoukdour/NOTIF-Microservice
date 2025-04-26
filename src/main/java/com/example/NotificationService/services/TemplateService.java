package com.example.NotificationService.services;

import com.example.NotificationService.dto.TemplateRequest;
import com.example.NotificationService.entities.Template;
import com.example.NotificationService.entities.TemplateVariable;
import com.example.NotificationService.repositories.TemplateRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    public Optional<Template> getTemplateByName(String name) {
        return templateRepository.findByName(name);
        
    }

    public Template saveTemplate(TemplateRequest templateDTO) {
        Template template = new Template();
        template.setName(templateDTO.getName());
        template.setLanguage(templateDTO.getLanguage());

        // 👉 Generate template path
        String templatePath = "src/main/resources/templates/" + templateDTO.getName() + ".html";
        template.setTemplatePath("/templates/" + templateDTO.getName() + ".html");

        // 👉 Create variables
        List<TemplateVariable> variableEntities = templateDTO.getVariables().stream()
                .map(varName -> {
                    TemplateVariable variable = new TemplateVariable();
                    variable.setVariableName(varName);
                    variable.setTemplate(template);
                    return variable;
                })
                .toList();

        template.setVariables(variableEntities);

        // 👉 Save to database
        Template savedTemplate = templateRepository.save(template);

        // 👉 Create the file physically
        createTemplateFile(templatePath);

        return savedTemplate;
    }

    private void createTemplateFile(String path) {
        try {
            File file = new File(path);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs(); // Create folders if not exist
            }
            if (file.createNewFile()) {
                // File created successfully
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("<html><body><!-- Template content here --></body></html>");
                }
            } else {
                System.out.println("File already exists: " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
