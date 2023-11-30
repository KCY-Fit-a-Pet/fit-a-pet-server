package com.kcy.fitapet;

import com.kcy.fitapet.global.common.repository.ExtendedJpaRepositoryFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = ExtendedJpaRepositoryFactory.class)

public class FitapetApplication {
	public static void main(String[] args) {
		SpringApplication.run(FitapetApplication.class, args);
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
}
