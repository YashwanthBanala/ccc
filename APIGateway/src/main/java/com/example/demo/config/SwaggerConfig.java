package com.example.demo.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi allServicesApi() {
        return GroupedOpenApi.builder()
                .group("all-services")
                .pathsToMatch("/**")
                .build();
    }
}
