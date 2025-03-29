package com.itda.moamoa.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
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