package com.itda.moamoa.global.security.jwt.filter;


import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.global.security.jwt.dto.CustomUserDetails;
import com.itda.moamoa.global.security.jwt.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //header에서 access 키의 토큰 꺼냄
        String accessToken = request.getHeader("access");

        //token 없는 경우
        if(accessToken == null){
            filterChain.doFilter(request, response); //권한 필요 없는 경우 존재하므로
            return;
        }

        //토큰 만료 여부 확인
        try{
            jwtUtil.isExpired(accessToken);
        } catch(ExpiredJwtException e){
            //만료 시에는 다음 필터로 넘기는 게 아님
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //access token 인지 category로 확인 - 아닐 시 응답, 다음 필터로 x
        String category = jwtUtil.getCategory(accessToken);
        if(!category.equals("access")) {
            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //검증된 토큰
        //토큰에서 username, role 획득
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

        //userEntity 생성해 값 set
        User user = User.builder()
                .username(username)
                //비밀번호는 보안상 token에 담기지 않음 -> JWT를 통한 인증 시 비밀번호 안담음 -> 생략
                .role(role)
                .build();

        //UserDetails 에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }


}
