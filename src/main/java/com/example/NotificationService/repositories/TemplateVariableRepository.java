package com.example.NotificationService.repositories;

import com.example.NotificationService.entities.TemplateVariable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TemplateVariableRepository extends JpaRepository<TemplateVariable, Long> {
    List<TemplateVariable> findByTemplateId(Long templateId);
}
