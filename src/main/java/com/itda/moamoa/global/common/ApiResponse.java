package com.itda.moamoa.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter                                     // Getter 자동 생성
@Setter                                     // Setter 자동 생성
@Builder                                    // Builder Pattern 제공
@JsonInclude(JsonInclude.Include.NON_NULL)  // JSON 직렬화에서 null 필드 제외
public class ApiResponse<T> {   // Data Type 자유
    private String status;
    private int code;
    private String message;
    private T data;             // 단일 응답 데이터
    private List<T> dtoList;    // 목록 응답 데이터
    private Integer size;       // Paging

    // SuccessCode를 활용한 단일 성공 응답 데이터 생성 메서드
    public static <T> ApiResponse<T> success(SuccessCode successCode, String message, T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .code(successCode.getCode())
                .message(message)
                .data(data)
                .build();
    }
    
    // 목록 응답 데이터를 생성 메서드
    public static <T> ApiResponse<T> successList(SuccessCode successCode, String message, List<T> dtoList, Integer size) {
        return ApiResponse.<T>builder()
                .status("success")
                .code(successCode.getCode())
                .message(message)
                .dtoList(dtoList)
                .size(size)
                .build();
    }
} 