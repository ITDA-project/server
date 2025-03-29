package com.itda.moamoa.global.security.jwt.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    private SecretKey secretKey; //JWT 생성, 검증 시 사용 키
    public JWTUtil(@Value("${spring.jwt.secret}") String secret){
        //application.yml jwt.secret 내용 byte로 가져옴
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token){
        //
        return Jwts.parser()
                .verifyWith(secretKey) //토큰 유효성 검증 - 서명 비교
                .build()
                .parseSignedClaims(token)//토큰 파싱
                .getPayload()//payload 정보
                .get("username", String.class); //username 추출
    }

    //user이 가지는 role - ADMIN
    public String getRole(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public Boolean isExpired(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
        //현재 시간보다 이전 = true(만료)
        //시간 남은 경우 false(유효 토큰)
    }

    //access, refresh token 구별
    public String getCategory(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("category", String.class);
    }

    //JWT 생성
    public String createJwt(String category, String username, String role, Long expiredMs){
        return Jwts.builder()
                .claim("category", category) //access, refresh
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) //만료시간
                .signWith(secretKey) //signature 추가
                .compact(); //JWT를 최종 문자열로 변환
    }

}