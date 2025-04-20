package com.example.NotificationService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ThymeleafConfig {

    @Bean
    public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    @Bean
    public ITemplateResolver templateResolver() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");            // Dossier contenant les templates
        resolver.setSuffix(".html");                 // Extension des fichiers
        resolver.setTemplateMode(TemplateMode.HTML); // Mode HTML
        resolver.setCharacterEncoding("UTF-8");      // Encodage
        resolver.setOrder(1);                        // Priorité si plusieurs resolvers
        resolver.setCacheable(false);                // Pour développement, désactive le cache
        return resolver;
    }
}
