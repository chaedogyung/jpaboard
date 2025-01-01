package com.jpaboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/").allowedOrigins("") // 허용할 도메인을 지정합니다.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드를 지정합니다.
                .allowedHeaders("") // 허용할 헤더를 지정합니다.
                .allowCredentials(true) // 자격 증명을 허용할지 여부를 지정합니다.
                .maxAge(3600); // 캐시 유효 기간을 지정합니다. }
    }
}