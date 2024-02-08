package kr.co.fitapet.domain.config;

import kr.co.fitapet.domain.DomainPackageLocation;
import kr.co.fitapet.domain.common.repository.ExtendedRepositoryFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "authorAwareAudit")
@EntityScan(basePackageClasses = DomainPackageLocation.class)
@EnableJpaRepositories(basePackageClasses = DomainPackageLocation.class, repositoryFactoryBeanClass = ExtendedRepositoryFactory.class)
public class JpaConfig {
}
