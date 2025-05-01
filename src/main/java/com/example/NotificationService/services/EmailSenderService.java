package com.example.NotificationService.services;

import com.example.NotificationService.models.SimpleMail;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailSenderService {

    @Autowired
    private  JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;


    public void sendMimeMessageEmail(MimeMessage message) throws Exception {
        mailSender.send(message);
        log.info("Email sent to: {}", message.getSender());
    }

    public void sendSimpleEmail(SimpleMail simpleMail) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setFrom(new InternetAddress(from, "🚀 Recruit AI"));
        helper.setTo(simpleMail.getTo());
        helper.setSubject(simpleMail.getSubject());
        helper.setText(simpleMail.getContent(), true);
        mailSender.send(mimeMessage);
        log.info("Email sent to: {}", simpleMail.getTo());
    }
}
