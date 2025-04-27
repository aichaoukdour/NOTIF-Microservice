package com.example.NotificationService.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    private Long id;

    @Column(name = "variable_name", nullable = false)
    private String variableName;

    @ManyToOne
    @JoinColumn(name = "template_id")
    @JsonIgnore
    private Template template;
}

