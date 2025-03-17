package com.itda.moamoa.global.security.jwt.filter;


import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.global.security.jwt.dto.CustomUserDetails;
import com.itda.moamoa.global.security.jwt.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //request에 대한 검증
        String authorization = request.getHeader("Authorization");

        //Authorization 헤더 검증
        if(authorization == null || authorization.startsWith("Bearer ")){
            System.out.println("token null");
            //필터 체인의 다음 필터로
            filterChain.doFilter(request, response);
            return;
        }

        //Bearer 부분 제거 후 순수 토큰 획득
        String token = authorization.split(" ")[1];

        //토큰 유효 시간 검증
        if (jwtUtil.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            return ;
        }

        //검증된 토큰
        //토큰에서 username, role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        //userEntity 생성해 값 set
        User user = User.builder()
                .username(username)
                .password("password") //token에 담기지 않음 -> 임시 비밀번호
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
