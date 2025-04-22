package com.example.NotificationService.dto;



import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class NotificationRequest {

    /** Adresse email du destinataire */
    @NotBlank(message = "L'email du destinataire est requis.")
    @Email(message = "Adresse email invalide.")
    @JsonProperty("to")
    private String name;

    /** Sujet de l'email */
    @NotBlank(message = "Le sujet de l'email est requis.")
    private String subject;

    /** Nom du template à utiliser (ex: otp, welcome, etc.) */
    @NotBlank(message = "Le nom du template est requis.")
    private String template;

    private Map<String, Object> variables;

    /** Le contenu de l'email */
    private String content;  // Add content field here
}
