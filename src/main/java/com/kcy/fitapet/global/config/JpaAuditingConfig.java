package com.kcy.fitapet.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "authorAwareAudit")
public class JpaAuditingConfig {

}
