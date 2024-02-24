package kr.co.fitapet.infra.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FcmConfig {
    private final ClassPathResource firebaseResource;

    public FcmConfig(@Value("${app.firebase.config.file}") String firebaseConfigPath) {
        this.firebaseResource = new ClassPathResource(firebaseConfigPath);
    }

    @Bean
    FirebaseApp firebaseApp() throws IOException {
        FirebaseOptions option = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(firebaseResource.getInputStream()))
                .build();
        return FirebaseApp.initializeApp(option);
    }

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        return FirebaseMessaging.getInstance(firebaseApp());
    }
}
