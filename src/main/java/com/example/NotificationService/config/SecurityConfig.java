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
            .csrf(csrf -> csrf.disable()) // Disable CSRF for testing purposes (e.g., Postman)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/notifications", "/api/v2/templates/*").permitAll()
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
