package com.example.NotificationService.services;

import com.example.NotificationService.dto.TemplateRequest;
import com.example.NotificationService.entities.Template;
import com.example.NotificationService.entities.TemplateVariable;
import com.example.NotificationService.repositories.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemplateService {

    @Autowired
    private TemplateRepository templateRepository;

    public Optional<Template> getTemplateByName(String name) {
        return templateRepository.findByName(name);
    }

    public Template saveTemplate(TemplateRequest templateDTO) {
        Template template = mapToTemplateEntity(templateDTO);
        Template savedTemplate = templateRepository.save(template);
        createTemplateFile(template.getTemplatePath());
        return savedTemplate;
    }

    private Template mapToTemplateEntity(TemplateRequest templateDTO) {
        Template template = new Template();
        template.setName(templateDTO.getName());
        template.setLanguage(templateDTO.getLanguage());
        template.setTemplatePath("/templates/" + templateDTO.getName() + ".html");

        List<TemplateVariable> variableEntities = templateDTO.getVariables().stream()
                .map(varName -> createTemplateVariable(varName, template))
                .toList();

        template.setVariables(variableEntities);
        return template;
    }

    private TemplateVariable createTemplateVariable(String varName, Template template) {
        TemplateVariable variable = new TemplateVariable();
        variable.setVariableName(varName);
        variable.setTemplate(template);
        return variable;
    }

    private void createTemplateFile(String relativePath) {
        String fullPath = "src/main/resources" + relativePath;
        try {
            File file = new File(fullPath);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            if (file.createNewFile()) {
                writeDefaultContentToFile(file);
            } else {
                System.out.println("File already exists: " + fullPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeDefaultContentToFile(File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("<html><body><!-- Template content here --></body></html>");
        }
    }
}
