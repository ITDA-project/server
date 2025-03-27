package com.itda.moamoa.domain.form.controller;

import com.itda.moamoa.domain.form.dto.FormRequestDTO;
import com.itda.moamoa.domain.form.dto.FormResponseDTO;
import com.itda.moamoa.domain.form.entity.Form;
import com.itda.moamoa.domain.form.service.FormApiService;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController             // REST API Controller
@RequiredArgsConstructor    // 필수 필드(final, @NonNull) 생성자 -> 의존성 주입 대체
public class FormApiController {
    private final FormApiService formApiService;

    // 해당 게시글에 제출된 신청폼 전체 조회
    @GetMapping("/api/posts/{postId}/form")         // URL 내 변수 사용, Token 내 Username(e-mail), HTTP Body 변수 사용
    public ResponseEntity<ApiResponse<Form>> getAllForms(@PathVariable long postId, @AuthenticationPrincipal String username, @RequestBody FormRequestDTO requestDto){
        // 1. 신청폼 조회를 Service 위임
        List<Form> got = formApiService.getAllForms(postId, username, requestDto);

        // 2. API Response 생성
        ApiResponse<Form> gotForms = ApiResponse.successList(
                SuccessCode.OK,
                "신청폼이 정상적으로 조회 되었습니다.",
                got,
                got.size());

        // 3. JSON 응답 반환
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gotForms);    // 상태 코드 200 반환
    }

    // 해당 게시글에 제출된 신청폼 개별 조회
    @GetMapping("/api/posts/{postId}/form/{formId}")         // URL 내 변수 사용, Token 내 Username(e-mail), HTTP Body 변수 사용
    public ResponseEntity<ApiResponse<Form>> getFormById(@PathVariable long postId, @PathVariable long formId, @AuthenticationPrincipal String username, @RequestBody FormRequestDTO requestDto){
        // 1. 신청폼 조회를 Service 위임
        Form got = formApiService.getFormById(postId, formId, username, requestDto);

        // 2. API Response 생성
        ApiResponse<Form> gotForm = ApiResponse.success(
                SuccessCode.OK,
                ".",
                got);

        // 3. JSON 응답 반환
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gotForm);     // 상태 코드 200 반환
    }

    // 신청폼 생성
    @PostMapping("/api/posts/{postId}/form")     // URL 내 변수 사용, HTTP Body 내 변수 사용, Token 받아 사용
public ResponseEntity<ApiResponse<FormResponseDTO>> create(@AuthenticationPrincipal String username, @PathVariable long post, @RequestBody FormRequestDTO requestDto){
        // 1. 신청폼 생성을 Service 위임
        FormResponseDTO created = formApiService.create(username, post, requestDto);

        // 2. API Response 생성
        ApiResponse<FormResponseDTO> createdForm = ApiResponse.success(
                SuccessCode.CREATED,
                "신청폼이 성공적으로 제출되었습니다.",
                created);

        // 3. JSON 응답 반환
        return ResponseEntity
                .status(HttpStatus.CREATED).
                body(createdForm);   // 상태 코드 201 반환
    }
}