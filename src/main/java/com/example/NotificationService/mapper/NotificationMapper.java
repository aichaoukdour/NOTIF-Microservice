package com.example.NotificationService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.example.NotificationService.dto.NotificationRequest;
import com.example.NotificationService.entities.Notification;
import com.example.NotificationService.entities.Template;
@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "recipientEmail", source = "to") // Mappage correct pour l'email du destinataire
    @Mapping(target = "sendDate", expression = "java(java.time.LocalDateTime.now())") // Date d'envoi actuelle
    @Mapping(target = "status", constant = "PENDING") // L'état initial est 'PENDING'
    @Mapping(target = "template", source = "template", qualifiedByName = "mapTemplate")  // Utilisation de la méthode personnalisée
    @Mapping(target = "content", source = "content") // Mappage du contenu
    @Mapping(target = "id", ignore = true) // L'ID est généré automatiquement
    Notification toEntity(NotificationRequest request);

    @Named("mapTemplate")  // Méthode personnalisée pour mapper le template
    default Template mapTemplate(String template) {
        if (template == null || template.isEmpty()) {
            throw new IllegalArgumentException("Template cannot be null or empty");
        }
        return new Template(template.trim()); // Assurez-vous que le modèle est bien trimé et mappé correctement
    }
}
