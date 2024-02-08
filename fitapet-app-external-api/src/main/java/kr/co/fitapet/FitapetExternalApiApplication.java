package kr.co.fitapet;

import jakarta.annotation.PostConstruct;
import kr.co.fitapet.domain.common.repository.ExtendedRepositoryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@SpringBootApplication
public class FitapetExternalApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FitapetExternalApiApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
