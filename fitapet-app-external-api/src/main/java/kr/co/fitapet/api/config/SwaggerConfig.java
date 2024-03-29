package kr.co.fitapet.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
@OpenAPIDefinition(
    servers = {
            @Server(url = "http://localhost:8082", description = "Local Server"),
            @Server(url = "${fitapet.domain.backend}", description = "Production Server")
    }
)
@RequiredArgsConstructor
public class SwaggerConfig {
    private static final String JWT = "jwtAuth";
    private final Environment environment;

    @Bean
    public OpenAPI openAPI() {
        String activeProfile = "";
        if (!ObjectUtils.isEmpty(environment.getActiveProfiles()) && environment.getActiveProfiles().length >= 1) {
            activeProfile = environment.getActiveProfiles()[0];
        }

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(JWT);

        return new OpenAPI()
                .info(apiInfo(activeProfile))
                .addServersItem(new io.swagger.v3.oas.models.servers.Server().url(""))
                .addSecurityItem(securityRequirement)
                .components(securitySchemes());
    }

    @Bean
    ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }

    private Components securitySchemes() {
        final var securitySchemeAccessToken = new SecurityScheme()
                .name(JWT)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        return new Components()
                .addSecuritySchemes(JWT, securitySchemeAccessToken);
    }

    private Info apiInfo(String activeProfile) {
        return new Info()
                .title("Fit a Pet API (" + activeProfile + ")")
                .description("Fit a Pet Backend API Docs")
                .version("1.0.0");
    }
}
