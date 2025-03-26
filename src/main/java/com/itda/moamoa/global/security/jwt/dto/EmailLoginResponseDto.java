package com.itda.moamoa.global.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class EmailLoginResponseDto {
    private String access;
    private String refresh;
    private String name; //본명
    private String role;
    private Long accessExpiresIn;
    private Long refreshExpiresIn;
}
