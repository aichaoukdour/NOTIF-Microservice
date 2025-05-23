package com.example.NotificationService.services;

import com.example.NotificationService.models.SimpleMail;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailSenderService {

    @Autowired
    private  JavaMailSender mailSender;

    public void sendMimeMessageEmail(MimeMessage message) throws Exception {
        mailSender.send(message);
        log.info("Email sent to: {}", message.getSender());
    }

    public void sendSimpleEmail(SimpleMail modelMail) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(modelMail.getTo());
        helper.setSubject(modelMail.getSubject());
        helper.setText(modelMail.getContent(), true);
        mailSender.send(mimeMessage);
        log.info("Email sent to: {}", modelMail.getTo());
    }
}
