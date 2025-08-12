package com.itda.moamoa.global.security.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itda.moamoa.global.security.jwt.dto.LoginDTO;
import com.itda.moamoa.global.security.jwt.entity.Refresh;
import com.itda.moamoa.global.security.jwt.repository.RefreshRepository;
import com.itda.moamoa.global.security.jwt.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final ObjectMapper objectMapper;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshRepository refreshRepository, ObjectMapper objectMapper){

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
        this.objectMapper = new ObjectMapper();
        setFilterProcessesUrl("/api/auth/login");

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginDTO loginDTO = objectMapper.readValue(request.getInputStream(), LoginDTO.class);

            String username = loginDTO.getUsername();
            String password = loginDTO.getPassword();

            System.out.println(username);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new AuthenticationException("failed to parse login request body") {
            };
        }
    }

    //로그인 성공 시 실행 (jwt 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        /*
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
         */

        String username = authentication.getName();

        String role = authentication.getAuthorities().iterator().next().getAuthority(); //시스템 상 권한 1개 admin

        String access = jwtUtil.createJwt("access", username, role, 36000000L); //1시간
        String refresh = jwtUtil.createJwt("refresh", username, role, 864000000L); //10일

        //생성한 refresh 토큰 db에 저장용
        addRefresh(username, refresh, 86400000L);

        //응답 설정
        //access 토큰은 헤더로
        response.setHeader("access", access);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); //인코딩
        response.getWriter().write("{\"refresh_token\": \"" + refresh + "\"}");

        response.setStatus(HttpStatus.OK.value()); //응답 코드

    }

    private void addRefresh(String username, String refresh, Long expiredMs){
        //만료일자
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refresh1 = Refresh.builder()
                .username(username)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refresh1);
    }


    //로그인 실패
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed){
        response.setStatus(401);
    }

}
