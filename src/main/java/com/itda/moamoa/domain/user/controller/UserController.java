package com.itda.moamoa.domain.user.controller;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.service.UserService;
import com.itda.moamoa.global.security.jwt.dto.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    /**
     * 회원 탈퇴 메소드
     * 탈퇴 후엔 로그아웃이 필요 -> refresh token 만료시키기
     */
    @DeleteMapping("/auth/delete")
    public ResponseEntity<?> userDelete(@AuthenticationPrincipal CustomUserDetails user, @RequestBody Map<String,String> requestBody, HttpServletResponse response){
        String refresh = requestBody.get("refresh_token");

        if (refresh == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            userService.deleteUserAndInvalidateToken(user, refresh);
            //클라이언트에서 refresh 토큰 제거 필요
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\": \"Refresh token removed\"}");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
