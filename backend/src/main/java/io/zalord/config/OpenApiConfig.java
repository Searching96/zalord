package io.zalord.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Zalord API",
        version = "v1",
        description = "Public HTTP API for Zalord services."
    )
)
public class OpenApiConfig {
}
