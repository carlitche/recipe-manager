package com.cookin.recipemanager;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info (
                title = "Recipe Manager Api",
                description = "REST Api Documentation for Recipe Manager Api",
                version = "1.0"
        ),
        servers = {
                @Server (
                        description = "local ENV",
                        url = "http://localhost:8080"
                )
        }
)
public class OpenApiConfig {
}
