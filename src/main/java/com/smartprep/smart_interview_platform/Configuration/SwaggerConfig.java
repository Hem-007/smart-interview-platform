package com.smartprep.smart_interview_platform.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart Interview Prep Platform API")
                        .description("Backend API documentation for the Smart Interview Prep Platform project")
                        .version("1.0.0"));
    }
}
