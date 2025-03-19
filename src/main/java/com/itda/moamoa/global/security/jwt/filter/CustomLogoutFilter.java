package com.itda.moamoa.global.security.jwt.filter;

import com.itda.moamoa.global.security.jwt.repository.RefreshRepository;
import com.itda.moamoa.global.security.jwt.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    
    public CustomLogoutFilter(JWTUtil jwtUtil, RefreshRepository refreshRepository){
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException{

        //모든 요청이 거쳐가는 필터 - 로그아웃 요청인지 확인
        //path 값을 확인
        String requestUri = request.getRequestURI();
        if(!requestUri.matches("^\\/logout$")){
            //로그아웃 요청 아닌 경우 -> 다음 필터로
            filterChain.doFilter(request, response);
            return;
        }
        //로그아웃 관련이더라도 post 아닌 경우
        String requestMethod = request.getMethod();
        if(!requestMethod.equals("POST")){
            //다음 필터로
            filterChain.doFilter(request, response);
            return;
        }

        //refresh 토큰 가져오기
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refresh")){
                refresh = cookie.getValue();
            }
        }

        //refresh 토큰 유효하지 않은 경우
        if(refresh == null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return ;
        }

        //토큰 만료 시 exception
        try{
            jwtUtil.isExpired(refresh);
        } catch(ExpiredJwtException e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //유효한 토큰의 payload category - refresh 종류인지 확인
        String category = jwtUtil.getCategory(refresh);
        if(!category.equals("refresh")){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //db에 해당 토큰이 저장되어있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if(!isExist){
            //이미 로그아웃한 상태인 경우
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        //로그아웃 진행
        //refresh 토큰 db에서 제거 - reissue 안되도록
        refreshRepository.deleteByRefresh(refresh);

        //refresh 토큰 null로 만들어 저장하기 위한 cookie
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0); //시간
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
