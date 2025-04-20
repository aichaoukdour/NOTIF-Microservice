package com.example.NotificationService.services;

import java.time.LocalDateTime;
import java.util.Map;

import org.jsoup.Jsoup;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.example.NotificationService.entities.Notification;
import com.example.NotificationService.entities.Template;
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

    public void sendTemplatedEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        log.debug("Sending templated email to {} with template {}", to, templateName);
        
        // Recherche du template dans la base de données
        Template template = templateRepository.findByName(templateName)
                .orElseThrow(() -> new TemplateNotFoundException(templateName));

        try {
            // Construction du contenu HTML avec Thymeleaf
            Context context = new Context();
            context.setVariables(variables);
            String body = templateEngine.process(templateName, context);

            // Préparation et envoi de l'e-mail
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true = contenu HTML

            mailSender.send(message);
            log.info("Email successfully sent to {}", to);

            // Sauvegarde dans la base de données
            Notification notif = new Notification();
            notif.setRecipientEmail(to);
            notif.setSubject(subject);
            notif.setContent(Jsoup.parse(body).text());
            notif.setSendDate(LocalDateTime.now());
            notif.setStatus("SENT");
            notif.setTemplate(template);

            notificationRepository.save(notif);
            log.debug("Notification saved to database");
            
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new EmailSendingException("Failed to process or send email: " + e.getMessage(), e);
        }
    }
}