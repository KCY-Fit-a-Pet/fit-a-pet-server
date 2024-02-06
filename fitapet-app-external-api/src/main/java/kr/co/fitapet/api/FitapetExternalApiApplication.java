package kr.co.fitapet.api;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class FitapetExternalApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FitapetExternalApiApplication.class, args);
    }
}
