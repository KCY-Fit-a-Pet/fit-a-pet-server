package com.kcy.fitapet.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private static String JWT = "jwtAuth";

    @Bean
    public OpenAPI openAPI() {
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(JWT);

        return new OpenAPI()
                .info(apiInfo())
                .addServersItem(new Server().url(""))
                .addSecurityItem(securityRequirement)
                .components(securitySchemes());
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

    private Info apiInfo() {
        return new io.swagger.v3.oas.models.info.Info()
                .title("Fit a Pet API")
                .description("Fit a Pet Backend API Docs")
                .version("1.0.0");
    }
}
