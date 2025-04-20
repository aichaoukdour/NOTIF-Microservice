package com.example.NotificationService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.example.NotificationService.dto.NotificationRequest;
import com.example.NotificationService.entities.Notification;
import com.example.NotificationService.entities.Template;
@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "recipientEmail", source = "to")
    @Mapping(target = "sendDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "template", source = "template", qualifiedByName = "mapTemplate")  // Using the custom mapping method
    @Mapping(target = "content", source = "content") // Ensure content is mapped from the request
    @Mapping(target = "id", ignore = true) // Assuming 'id' is auto-generated
    Notification toEntity(NotificationRequest request);

    @Named("mapTemplate")  // Add the @Named annotation so MapStruct knows which method to use
    default Template mapTemplate(String template) {
        if (template == null || template.isEmpty()) {
            throw new IllegalArgumentException("Template cannot be null or empty");
        }
        return new Template(template.trim()); // Ensure the template string is trimmed before mapping
    }
}
