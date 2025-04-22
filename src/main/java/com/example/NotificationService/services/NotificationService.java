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

    public NotificationResponse sendTemplatedEmail(NotificationRequest request) {
        String to = request.getTo();
        String subject = request.getSubject();
        String templateName = request.getTemplate();
        Map<String, Object> variables = request.getVariables();

        log.debug("Preparing to send templated email to: {}, template: {}", to, templateName);

        Template template = templateRepository.findByName(templateName)
                .orElseThrow(() -> new TemplateNotFoundException(templateName));

        // ✅ Validate email address
        if (!EmailValidator.getInstance().isValid(to)) {
            log.warn("Invalid email address: {}", to);
            saveNotification(to, subject, "Invalid email address: " + to, NotificationStatus.FAILED, template);
            return new NotificationResponse(to, "Invalid email address", NotificationStatus.FAILED);
        }

        try {
            // ✅ Generate HTML email content
            Context context = new Context();
            context.setVariables(variables);
            String body = templateEngine.process(templateName, context);
            String plainText = Jsoup.parse(body).text();

            // ✅ Create and send the email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
            log.info("Email successfully sent to {}", to);

            // ✅ Save successful notification
            saveNotification(to, subject, plainText.isBlank() ? "(No content generated)" : plainText, NotificationStatus.SENT, template);

            return new NotificationResponse(to, plainText, NotificationStatus.SENT);

        } catch (MailSendException e) {
            log.error("Mail sending exception occurred: {}", e.getMessage());
            saveNotification(to, subject, "Error sending mail: " + e.getMessage(), NotificationStatus.FAILED, template);
            throw new EmailSendingException("Failed to send email", e);

        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            saveNotification(to, subject, "Error: " + e.getMessage(), NotificationStatus.FAILED, template);
            throw new EmailSendingException("Failed to process or send email", e);
        }
    }

    private void saveNotification(String to, String subject, String content, NotificationStatus status, Template template) {
        Notification notif = new Notification();
        notif.setRecipientEmail(to);
        notif.setSubject(subject != null ? subject : "(No subject)");
        notif.setContent(content != null ? content : "(No content)");
        notif.setSendDate(LocalDateTime.now());
        notif.setStatus(status);
        notif.setTemplate(template);

        notificationRepository.save(notif);
        log.debug("Notification saved with status: {}", status);
    }
}
