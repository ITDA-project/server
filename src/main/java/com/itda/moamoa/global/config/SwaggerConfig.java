package com.itda.moamoa.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("MOAMOA API")
                .version("v1.0")
                .description("MOAMOA 프로젝트 API 문서");

        // JWT 인증 방식 정의
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("access");

        // 보안 요구사항 추가
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("accessAuth");

        return new OpenAPI()
                .info(info)
                .components(new Components().addSecuritySchemes("accessAuth", securityScheme))
                .addSecurityItem(securityRequirement);
    }
} 