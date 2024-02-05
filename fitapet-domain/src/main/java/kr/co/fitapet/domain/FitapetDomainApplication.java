package kr.co.fitapet.domain;


import jakarta.annotation.PostConstruct;
import kr.co.fitapet.domain.common.repository.ExtendedRepositoryFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = ExtendedRepositoryFactory.class)
public class FitapetDomainApplication {
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
