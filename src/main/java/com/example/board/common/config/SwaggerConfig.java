package com.example.board.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(getInfo())
                .components(new Components());
    }

    public Info getInfo() {
        return new Info()
                .description("게시판 API Swagger")
                .title("게시판 API Swagger")
                .version("1.0");
    }
}
