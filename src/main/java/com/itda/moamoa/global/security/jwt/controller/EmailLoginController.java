package com.itda.moamoa.global.security.jwt.controller;

import com.itda.moamoa.domain.user.service.UserService;
import com.itda.moamoa.global.security.jwt.dto.EmailLoginDto;
import com.itda.moamoa.global.security.jwt.dto.EmailLoginResponseDto;
import com.itda.moamoa.global.security.jwt.service.RefreshService;
import com.itda.moamoa.global.security.jwt.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class EmailLoginController {
    // username, password 받아 검증된 요청에 대해 jwt 발급, 응답 반환
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final RefreshService refreshService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody EmailLoginDto emailLoginDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_ADMIN"); //ROLE_DEFAULT


        Long accessExpireMs = 3600000L;
        Long refreshExpireMs = 864000000L;

        String access = jwtUtil.createJwt("access", username, role, accessExpireMs); //1시간
        String refresh = jwtUtil.createJwt("refresh", username, role, refreshExpireMs); //실제 토큰 값 String

        //refresh entity는 refresh token과 용도 다름
        //role 생략한 refresh entity를 담아 refresh repository에서 관리할 수 있도록 함
        refreshService.addRefresh(refresh, username, refreshExpireMs);

        String name = userService.findNameByUsername(username);

        //응답 생성
        //dto에서 담아줄 내용 - 각 토큰 값, 만료시간, name(본명), role
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", access);

        EmailLoginResponseDto response = new EmailLoginResponseDto(
                access,
                refresh,
                name, //userService에서 가져온 본명
                role,
                accessExpireMs/1000, //초 단위로 변환
                refreshExpireMs/1000
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(response);

    }

}
