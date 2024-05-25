package com.example.kotrip.config;

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
        String jwt = "JWT";

        // API 보안 요구사항 정의
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme()
                .name(jwt)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"));

        // 보안 요구사항 + API 기본 정보 return
        return new OpenAPI()
                .info(apiInfo())
                .components(components)
                .addSecurityItem(securityRequirement);
    }


    private Info apiInfo() {

        // API 기본 정보 객체 return
        return new Info()
                .title("Kotrip 서버 API 명세서")
                .description("api를 문서화하고, 팀원들 간의 협업 및 테스트를 용이하도록 한다.")
                .version("1.0.0");
    }

}
