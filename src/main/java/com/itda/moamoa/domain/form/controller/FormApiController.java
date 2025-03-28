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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/form")
public class FormApiController {
    private final FormApiService formApiService;

    // 해당 게시글에 제출된 신청폼 전체 조회
    @GetMapping
    public ResponseEntity<ApiResponse<Form>> getAllForms(@PathVariable long postId, @AuthenticationPrincipal String username, @RequestBody FormRequestDTO requestDto){
        List<Form> got = formApiService.getAllForms(postId, username, requestDto);

        ApiResponse<Form> gotForms = ApiResponse.successList(
                SuccessCode.OK,
                "해당 게시글에 제출된 신청폼이 정상적으로 조회 되었습니다.",
                got,
                got.size());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gotForms);
    }

    // 해당 게시글에 제출된 신청폼 개별 조회
    @GetMapping("/{formId}")
    public ResponseEntity<ApiResponse<Form>> getFormById(@PathVariable long postId, @PathVariable long formId, @AuthenticationPrincipal String username, @RequestBody FormRequestDTO requestDto){
        Form got = formApiService.getFormById(postId, formId, username, requestDto);

        ApiResponse<Form> gotForm = ApiResponse.success(
                SuccessCode.OK,
                "신청폼이 정상적으로 조회되었습니다.",
                got);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gotForm);
    }

    // 신청폼 생성
    @PostMapping
    public ResponseEntity<ApiResponse<FormResponseDTO>> create(@AuthenticationPrincipal String username, @PathVariable long post, @RequestBody FormRequestDTO requestDto){
        FormResponseDTO created = formApiService.create(username, post, requestDto);

        ApiResponse<FormResponseDTO> createdForm = ApiResponse.success(
                SuccessCode.CREATED,
                "신청폼이 성공적으로 제출되었습니다.",
                created);

        return ResponseEntity
                .status(HttpStatus.CREATED).
                body(createdForm);
    }
}