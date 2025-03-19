package com.itda.moamoa.global.security.jwt.controller;

import com.itda.moamoa.global.security.jwt.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //service로 분리할 코드 있음
@ResponseBody
public class ReissueController {
    private final JWTUtil jwtUtil;

    public ReissueController(JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response){
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            //쿠키에 refresh key 값이 있다면
            if(cookie.getName().equals("refresh")){
                refresh = cookie.getValue();
            }
        }

        //값 없는 경우
        if(refresh == null){
            //response body 에 내용 넣음
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //만료 시 exception 발생
        try{
            jwtUtil.isExpired(refresh);
        } catch(ExpiredJwtException e){
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        //refresh 토큰인지 payload category 값으로 확인
        String category = jwtUtil.getCategory(refresh);
        if(!category.equals("refresh")){
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        //refresh 토큰에서 access 토큰 생성용 값 얻음
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //access 토큰 생성
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        //access 토큰은 헤더 설정
        response.setHeader("access", newAccess);
        //refresh 토큰은 쿠키로
        response.addCookie(createCookie("refresh", newRefresh));


        //응답 코드
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //쿠키 생성 메서드 
    private Cookie createCookie(String key, String value){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        return cookie;
    }

}
