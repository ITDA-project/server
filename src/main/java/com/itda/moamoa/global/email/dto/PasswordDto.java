package com.itda.moamoa.global.email.dto;

import lombok.Getter;

@Getter
public class PasswordDto {
    private String email;
    private Integer otpNumber;
    private String password;
}
