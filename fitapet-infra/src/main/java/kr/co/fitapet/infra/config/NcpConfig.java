package kr.co.fitapet.infra.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "cloud.aws")
@ConfigurationPropertiesBinding
public class NcpConfig {
    private Credentials credentials;
    private S3 s3;
    private Map<String, String> region;

    public record Credentials(String accessKey, String secretKey) {}
    public record S3(String endPoint, String bucket) {}

    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(this.credentials.accessKey(), this.credentials.secretKey());

        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AmazonS3ClientBuilder.EndpointConfiguration(s3.endPoint(), region.get("static")))
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
