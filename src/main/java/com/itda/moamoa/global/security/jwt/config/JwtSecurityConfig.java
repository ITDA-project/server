package com.itda.moamoa.global.security.jwt.config;

import com.itda.moamoa.global.security.jwt.filter.JWTFilter;
import com.itda.moamoa.global.security.jwt.filter.LoginFilter;
import com.itda.moamoa.global.security.jwt.util.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class JwtSecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    public JwtSecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil){
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
    //비밀번호 인코딩
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //csrf disable - session의 경우 필수 방어 필요, jwt는 stateless
        http
                .csrf((auth) -> auth.disable());
        //form login disable
        http
                .formLogin((auth) -> auth.disable());

        //http basic disable
        http
                .httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth)->auth
                        .requestMatchers("/login","/","/join").permitAll()

                        //권한 가진 사람
                        .requestMatchers("/admin").hasRole("ADMIN")

                        //로그인 사용자만
                        .anyRequest().authenticated());

        //JWT 검증 필터 추가
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        //로그인 필터 추가
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //세션 설정 -stateless로 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();


    }
}
