package com.itda.moamoa.global.common;

import com.itda.moamoa.global.common.ErrorCode;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;

@Getter                                     // Getter 자동 생성
@Setter                                     // Setter 자동 생성
@Builder                                    // Builder 제공
public class ErrorResponse {
    private final String status;
    private final int code;
    private final String message;
    private List<FieldError> errors;


    @Getter                 // Getter 자동 생성
    @AllArgsConstructor     // 생성자 생성
    @NoArgsConstructor      // 기본 생성자 생성
    public static class FieldError {
        private String field;       // 문제가 발생한 필드
        private String message;
    }

    // 단순 에러
    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status("error")
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    // 유효성 검사 실패
    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors().stream()
                .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        
        return ErrorResponse.builder()
                .status("error")
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .errors(fieldErrors)
                .build();
    }

    // 메시지 커스터마이징
    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .status("error")
                .code(errorCode.getCode())
                .message(message)
                .build();
    }

    // 수동으로 여러 오류 추가
    public static ErrorResponse of(ErrorCode errorCode, List<FieldError> errors) {
        return ErrorResponse.builder()
                .status("error")
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .errors(errors)
                .build();
    }
}