package com.itda.moamoa.global.security.jwt.controller;

import com.itda.moamoa.global.security.jwt.entity.Refresh;
import com.itda.moamoa.global.security.jwt.repository.RefreshRepository;
import com.itda.moamoa.global.security.jwt.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Controller //service로 분리할 코드 있음
@ResponseBody
public class ReissueController {
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public ReissueController(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    @PostMapping("/reissue")
    public void reissue(@RequestBody Map<String, String> requestBody, HttpServletResponse response)throws IOException{
        //request body(json 본문)에서 refresh 토큰 추출
        String refresh = requestBody.get("refresh_token");

        //토큰값 없는 경우
        if(refresh == null || refresh.isEmpty()){
            response.setStatus(response.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"refresh token null\"}");
        }

        //토큰 만료 시 exception 발생
        try{
            jwtUtil.isExpired(refresh);
        } catch(ExpiredJwtException e){
            response.setStatus(response.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"refresh token expired\"}");
        } catch(Exception e){
            response.setStatus(response.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"refresh token invalid\"}");
        }

        //refresh 토큰인지 payload category 값으로 확인
        String category = jwtUtil.getCategory(refresh);
        if(!category.equals("refresh")){
            response.setStatus(response.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"refresh token invalid\"}");
        }

        //db에 저장되어 있는지 확인 -> 유효한 refresh 토큰이 됨
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if(!isExist){
            response.setStatus(response.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"refresh token invalid\"}");
        }

        //refresh 토큰에서 access 토큰 생성용 값 얻음
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        //access 토큰 생성
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        //db에 기존 refresh 토큰 삭제
        refreshRepository.deleteByRefresh(refresh);

        //새 refresh 토큰 저장
        addRefresh(username, newRefresh, 86400000L); //새 refresh 넣음

        //access 토큰은 헤더 설정
        response.setHeader("access", newAccess);

        //refresh 토큰은 application/json으로
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); //인코딩
        response.getWriter().write("{\"refresh_token\": \"" + newRefresh + "\"}");
        response.setStatus(HttpServletResponse.SC_OK); //200

    }

    //refresh 추가 메서드
    private void addRefresh(String username, String refresh, Long expiredMs){
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        Refresh refresh1 = Refresh.builder()
                .username(username)
                .refresh(refresh)
                .expiration(date.toString())
                .build();

        refreshRepository.save(refresh1);
    }
}
