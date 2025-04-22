package com.example.NotificationService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.example.NotificationService.dto.NotificationRequest;
import com.example.NotificationService.entities.Notification;
import com.example.NotificationService.entities.Template;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "recipientEmail", source = "email")
    @Mapping(target = "sendDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "template", source = "template", qualifiedByName = "mapTemplate")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "id", ignore = true)
    Notification toEntity(NotificationRequest request);

    @Named("mapTemplate")
    default Template mapTemplate(String template) {
        if (template == null || template.isEmpty()) {
            throw new IllegalArgumentException("Template cannot be null or empty");
        }
        return new Template(template.trim());
    }
}
