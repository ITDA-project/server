package com.itda.moamoa.global.security.jwt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itda.moamoa.global.security.jwt.filter.CustomLogoutFilter;
import com.itda.moamoa.global.security.jwt.filter.JWTFilter;
import com.itda.moamoa.global.security.jwt.filter.LoginFilter;
import com.itda.moamoa.global.security.jwt.repository.RefreshRepository;
import com.itda.moamoa.global.security.jwt.util.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
public class JwtSecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final ObjectMapper objectMapper;

    public JwtSecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, RefreshRepository refreshRepository, ObjectMapper objectMapper){
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
        this.objectMapper = objectMapper;
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
                        .requestMatchers("/login", "/join").permitAll()
                        .requestMatchers("/auth/login","/", "/auth/signup/**", "/auth").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/posts", "/api/posts/{postId}").permitAll() //GET 요청 허용
                        .requestMatchers(HttpMethod.POST, "/posts/search/**").permitAll()

                        //권한 가진 사람
                        .requestMatchers("/admin").hasRole("ADMIN")

                        //access token 만료 -> 로그인 불가하므로
                        .requestMatchers("/reissue").permitAll()

                        //로그인 사용자만
                        .anyRequest().authenticated());

        //JWT 검증 필터 추가
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        //로그인 필터 추가
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository, objectMapper), UsernamePasswordAuthenticationFilter.class);

        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);

        //세션 설정 -stateless로 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();


    }
}
