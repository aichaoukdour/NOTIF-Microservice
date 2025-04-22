package com.example.NotificationService.services;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.commons.validator.routines.EmailValidator;
import org.jsoup.Jsoup;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.NotificationService.dto.NotificationRequest;
import com.example.NotificationService.dto.NotificationResponse;
import com.example.NotificationService.entities.Notification;
import com.example.NotificationService.entities.Template;
import com.example.NotificationService.enums.NotificationStatus;
import com.example.NotificationService.exception.EmailSendingException;
import com.example.NotificationService.exception.TemplateNotFoundException;
import com.example.NotificationService.repositories.NotificationRepository;
import com.example.NotificationService.repositories.TemplateRepository;
import com.example.NotificationService.mapper.NotificationMapper;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final TemplateRepository templateRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationResponse sendTemplatedEmail(NotificationRequest request) {
        String to = request.getName();
        String subject = request.getSubject();
        String templateName = request.getTemplate();
        Map<String, Object> variables = request.getVariables();

        log.debug("Preparing to send templated email to: {}, template: {}", to, templateName);

        // Validate template and email address
        Template template = validateTemplateAndEmail(to, templateName);

        // Generate and send the email
        try {
            String body = generateEmailBody(templateName, variables);
            MimeMessage message = createMimeMessage(to, subject, body);
            mailSender.send(message);

            // Save notification and return response using the mapper
            return saveNotificationAndReturnResponse(request, body, template, NotificationStatus.SENT);
        } catch (Exception e) {
            return handleError(to, subject, e, template);
        }
    }

    private Template validateTemplateAndEmail(String to, String templateName) {
        if (!isValidEmail(to)) {
            throw new EmailSendingException("Invalid email address: " + to);
        }
        return templateRepository.findByName(templateName)
                .orElseThrow(() -> new TemplateNotFoundException(templateName));
    }

    private boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    private String generateEmailBody(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }

    private MimeMessage createMimeMessage(String to, String subject, String body) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        return message;
    }

    private NotificationResponse saveNotificationAndReturnResponse(NotificationRequest request, String body, Template template, NotificationStatus status) {
        String plainText = Jsoup.parse(body).text();
        // Use the mapper to convert the NotificationRequest into a Notification entity
        Notification notification = notificationMapper.toEntity(request);
        notification.setContent(plainText != null ? plainText : "(No content)");
        notification.setSendDate(LocalDateTime.now());
        notification.setStatus(status);
        notification.setTemplate(template);
        notificationRepository.save(notification);

        log.debug("Notification saved with status: {}", status);
        return new NotificationResponse(request.getName(), plainText, status);
    }

    private NotificationResponse handleError(String to, String subject, Exception e, Template template) {
        String errorMessage = (e instanceof MailSendException) ? "Error sending email: " + e.getMessage() : "Unexpected error: " + e.getMessage();
        log.error(errorMessage);
        saveNotification(to, subject, errorMessage, NotificationStatus.FAILED, template);
        return new NotificationResponse(to, errorMessage, NotificationStatus.FAILED);
    }

    private void saveNotification(String to, String subject, String content, NotificationStatus status, Template template) {
        Notification notification = new Notification();
        notification.setRecipientEmail(to);
        notification.setSubject(subject != null ? subject : "(No subject)");
        notification.setContent(content != null ? content : "(No content)");
        notification.setSendDate(LocalDateTime.now());
        notification.setStatus(status);
        notification.setTemplate(template);

        notificationRepository.save(notification);
        log.debug("Notification saved with status: {}", status);
    }
}
