package com.itda.moamoa.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 테스트를 위해 모든 보안 비활성화
        http
            .csrf().disable()
            .cors().disable()
            .authorizeHttpRequests()
                .anyRequest().permitAll(); // 모든 요청에 대해 인증 없이 접근 허용
        
        return http.build();
    }
}
