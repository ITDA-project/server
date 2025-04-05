package com.itda.moamoa.domain.user.controller;

import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * 회원 탈퇴 메소드
     * 탈퇴 후엔 로그아웃이 필요 -> 토큰들 다 만료시키기
     */
    @DeleteMapping("/auth/delete")
    public ResponseEntity<?> userDelete(@AuthenticationPrincipal(expression = "user") User user){
        userService.deleteUser(user);
        // access, refresh token 무효화
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
