package com.itda.moamoa.global.security.jwt.filter;

import com.itda.moamoa.global.security.jwt.dto.CustomUserDetails;
import com.itda.moamoa.global.security.jwt.entity.Refresh;
import com.itda.moamoa.global.security.jwt.repository.RefreshRepository;
import com.itda.moamoa.global.security.jwt.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private RefreshRepository refreshRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshRepository refreshRepository){
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println(username);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공 시 실행 (jwt 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication){
        /*
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
         */

        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createJwt("access", username, role, 6000000L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 864000000L);

        addRefresh(username, refresh, 86400000L);

        //응답 설정
        response.setHeader("access", access); //response.addHeader("Authorization", "Bearer " + token);
        response.addCookie(createCookie("refresh", refresh)); //
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

    //key, JWT value 로 쿠키를 생성
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60); //refresh token과 동일

        //cookie.setSecure(true); //https 통신 시
        //cookie.setPath("/"); //특정 경로

        cookie.setHttpOnly(true); //js로 접근 x
        return cookie;
    }

    //로그인 실패
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed){
        response.setStatus(401);
    }

}
