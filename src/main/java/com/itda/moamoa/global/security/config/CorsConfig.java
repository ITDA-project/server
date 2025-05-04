package com.itda.moamoa.global.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); //addAllowedOrigin 에 "*" 불가
        config.addAllowedOrigin("http://localhost:3000"); //서버 외 요청 출처
        config.addAllowedHeader("*"); //클라이언트로부터 요청받는 헤더
        config.addAllowedMethod("*"); //클라이언트로부터 받는 메서드
        config.addExposedHeader("Authorization"); //클라이언트에게 응답하는 헤더

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); //url에 cors 설멍 매핑용 객체
        source.registerCorsConfiguration("/**", config); //모든 경로에 cors 설정
        return source;
    }
}
