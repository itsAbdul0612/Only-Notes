package com.technerd.onlyNotes.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myCustomization(){
        return new OpenAPI().info(
                new Info().title("Only Notes App API")
                        .description("by Abdul Rahman")
        ).servers(List.of(
                new Server().url("http://localhost:8080").description("Local"),
                new Server().url("http://localhost:8081").description("Production"))
        ).tags(List.of(
                new Tag().name("User API"),
                new Tag().name("Notes API"))
        );
    }
}
