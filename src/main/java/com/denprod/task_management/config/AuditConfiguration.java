package com.denprod.task_management.config;

import com.denprod.task_management.entity.security.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfiguration {
    @Bean
    AuditorAware<User> auditorProvider() {
        return new SecurityAuditorAware();
    }
}
