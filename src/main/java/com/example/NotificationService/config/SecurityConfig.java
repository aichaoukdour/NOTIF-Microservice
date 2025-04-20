package com.example.NotificationService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Important pour POST depuis Postman
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/notifications").permitAll()
                .requestMatchers("/api/v1/notifications/email").permitAll()
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
