package com.example.NotificationService.listeners;

import com.example.NotificationService.models.SimpleMail;
import com.example.NotificationService.services.EmailSenderService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class EmailReceiverService {
    @Autowired
    private  EmailSenderService emailSenderService;

    @RabbitListener(queues = "email_queue")
    public void receiveMessage(SimpleMail simpleMail) throws Exception {
        emailSenderService.sendSimpleEmail(simpleMail);  // Send the email
    }
}
