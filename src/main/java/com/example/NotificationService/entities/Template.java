package com.example.NotificationService.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "template")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Change from String to Long

    private String name;
    private String language;
    
    // Remplacer "content" par "templatePath"
    @Column(name = "template_path")
    private String templatePath;

    public Template(String templatePath) {
        this.templatePath = templatePath;
    }


    
}
