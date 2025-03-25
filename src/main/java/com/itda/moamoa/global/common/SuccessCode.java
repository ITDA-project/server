package com.itda.moamoa.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter                     // Getter 자동 생성
@RequiredArgsConstructor    // 필수 필드로만 생성자 생성
public enum SuccessCode {

    OK(HttpStatus.OK, 200),
    CREATED(HttpStatus.CREATED, 201);

    private final HttpStatus httpStatus;
    private final int code;
    
}