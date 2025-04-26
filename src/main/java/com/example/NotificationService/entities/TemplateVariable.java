package com.example.NotificationService.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "template_variables")
public class TemplateVariable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "variable_name", nullable = false)
    private String variableName;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;
}

