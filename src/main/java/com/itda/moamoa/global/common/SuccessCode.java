package com.itda.moamoa.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    OK(HttpStatus.OK, 200),
    CREATED(HttpStatus.CREATED, 201);

    private final HttpStatus httpStatus;
    private final int code;
    
}