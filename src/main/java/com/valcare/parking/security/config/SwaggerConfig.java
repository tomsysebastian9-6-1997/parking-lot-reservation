package com.valcare.parking.security.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "basicAuth";

        return new OpenAPI()
                .info(new Info().title("Parking Lot API").version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .schemaRequirement(securitySchemeName,
                        new SecurityScheme().name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic"));
    }
}

