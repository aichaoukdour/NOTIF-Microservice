package com.example.NotificationService.services;

import com.example.NotificationService.models.SimpleMail;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailQueuingService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.email.name}")
    private String emailExchange;

    @Value("${rabbitmq.binding.email.name}")
    private String emailRoutingKey;

    public void dispatchEmail(MimeMessage message) throws Exception {
        rabbitTemplate.convertAndSend(emailExchange, emailRoutingKey, message);
    }

    public void dispatchEmail(SimpleMail message) throws Exception {
        rabbitTemplate.convertAndSend(emailExchange, emailRoutingKey, message);
    }
}
