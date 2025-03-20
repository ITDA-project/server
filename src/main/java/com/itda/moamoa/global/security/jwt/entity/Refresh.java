package com.itda.moamoa.global.security.jwt.entity;

import jakarta.persistence.*;
import lombok.*;
//refresh 토큰 db에 저장 - 탈취 시 강제 만료 처리
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Refresh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String refresh;
    private String expiration;
}
