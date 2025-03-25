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
    private T data;
    private List<T> dtoList;
    private Integer size;

    // SuccessCode를 활용한 성공 응답 생성 메서드 추가
    public static <T> ApiResponse<T> success(SuccessCode successCode, String message, T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .code(successCode.getCode())
                .message(message)
                .data(data)
                .build();
    }
    
    // 리스트 응답을 위한 메서드 추가
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