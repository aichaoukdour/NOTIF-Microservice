package com.example.NotificationService.services;

import com.example.NotificationService.dto.NotificationRequest;
import com.example.NotificationService.dto.NotificationResponse;
import com.example.NotificationService.entities.Notification;
import com.example.NotificationService.entities.Template;
import com.example.NotificationService.entities.FailedNotificationLog;
import com.example.NotificationService.enums.NotificationStatus;
import com.example.NotificationService.exception.TemplateNotFoundException;
import com.example.NotificationService.mapper.NotificationMapper;
import com.example.NotificationService.models.SimpleMail;
import com.example.NotificationService.repositories.FailedNotificationLogRepository;
import com.example.NotificationService.repositories.NotificationRepository;
import com.example.NotificationService.repositories.TemplateRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final TemplateEngine templateEngine;
    private final TemplateRepository templateRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final FailedNotificationLogRepository failedNotificationLogRepository;
    private final EmailQueuingService emailQueuingService;

    public NotificationResponse sendTemplatedEmail(@RequestBody @Valid NotificationRequest request) throws Exception {
        String email = request.getEmail();
        String subject = request.getSubject();
        String templateName = request.getTemplate();
        Map<String, Object> variables = request.getVariables();

        log.info("Sending email to: {}, template: {}", email, templateName);

        if (!isValidEmail(email)) {
            return handleError(request, "Invalid email address: " + email);
        }

        // Validate required variables for the template
        if (!validateTemplateVariables(templateName, variables)) {
            return handleError(request, "Missing required variables for template: " + templateName);
        }

        try {
            Template template = templateRepository.findByName(templateName)
                .orElseThrow(() -> new TemplateNotFoundException(templateName));

            String htmlBody = createEmailContent(templateName, variables);
            sendEmail(email, subject, htmlBody);

            return saveNotification(request, htmlBody, template, NotificationStatus.SENT);
        } catch (TemplateNotFoundException e) {
            return handleError(request, "Failed: Template not found");
        } catch (Exception e) {
            throw e;
        }
    }

    private boolean isValidEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
        log.error("Email address is null or empty");
        return false;
    }
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    if (!email.matches(emailRegex)) {
        log.error("Invalid email address format: {}", email);
        return false;
    }
    return true;
}

    private boolean validateTemplateVariables(String templateName, Map<String, Object> variables) {
        List<String> requiredVariables;
        switch (templateName) {
            case "application_confirmation":
                requiredVariables = List.of("candidateName", "jobTitle", "companyName", "applicationId", "supportEmail", "platformName", "logoUrl");
                break;
            case "application_status_update":
                requiredVariables = List.of("candidateName", "jobTitle", "companyName", "applicationId", "status", "statusDetails", "supportEmail", "platformName", "logoUrl");
                break;
            case "job_posting_confirmation":
                requiredVariables = List.of("recruiterName", "jobTitle", "jobId", "paymentLink", "amountDue", "dueDate", "platformName", "supportEmail", "logoUrl");
                break;
            default:
                log.error("Unknown template: {}", templateName);
                return false;
        }
        return variables != null && requiredVariables.stream().allMatch(variables::containsKey);
    }

    private NotificationResponse handleError(NotificationRequest request, String errorMessage) {
        log.error("Error occurred while processing request: {}", errorMessage);

        Notification notification = notificationMapper.toEntity(request);
        notification.setStatus(NotificationStatus.FAILED);
        notification.setContent(errorMessage);
        notificationRepository.save(notification);

        FailedNotificationLog failedNotificationLog = new FailedNotificationLog(notification, errorMessage);
        failedNotificationLogRepository.save(failedNotificationLog);

        return NotificationResponse.builder()
            .recipientEmail(request.getEmail())
            .subject(errorMessage)
            .content(errorMessage)
            .status(NotificationStatus.FAILED)
            .templateName(request.getTemplate())
            .build();
    }

    private String createEmailContent(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        if (variables != null) {
            context.setVariables(variables);
        }
        return templateEngine.process(templateName, context);
    }

    private void sendEmail(String email, String subject, String htmlBody) throws Exception {
        SimpleMail simpleMail = SimpleMail.builder()
            .to(email)
            .subject(subject)
            .content(htmlBody)
            .build();
        emailQueuingService.dispatchEmail(simpleMail);
        log.info("Email sent dispatch: {}", email);
    }

    private NotificationResponse saveNotification(@RequestBody @Valid NotificationRequest request, String htmlBody, 
                                             Template template, NotificationStatus status) {
    String plainText = htmlBody != null ? Jsoup.parse(htmlBody).text() : "";
    // Truncate content to 255 characters if necessary
    if (plainText.length() > 255) {
        plainText = plainText.substring(0, 255);
        log.warn("Content truncated to 255 characters for notification to: {}", request.getEmail());
    }

    Notification notification = notificationMapper.toEntity(request);
    notification.setRecipientEmail(request.getEmail());
    notification.setContent(plainText);
    notification.setSendDate(LocalDateTime.now());
    notification.setStatus(status);
    notification.setTemplate(template);
    notificationRepository.save(notification);

    return NotificationResponse.builder()
        .recipientEmail(request.getEmail())
        .subject(status == NotificationStatus.SENT ? "Success" : "Failed")
        .content(plainText)
        .status(status)
        .templateName(template.getName())
        .sendDate(LocalDateTime.now())
        .build();
}}