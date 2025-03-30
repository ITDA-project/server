package com.itda.moamoa.domain.form.controller;

import com.itda.moamoa.domain.form.dto.FormListResponseDTO;
import com.itda.moamoa.domain.form.dto.FormRequestDTO;
import com.itda.moamoa.domain.form.dto.FormResponseDTO;
import com.itda.moamoa.domain.form.service.FormApiService;
import com.itda.moamoa.domain.participant.entity.Participant;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import com.itda.moamoa.global.security.jwt.dto.CustomUserDetails;
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
    public ResponseEntity<ApiResponse<FormListResponseDTO>> getAllForms(
            @PathVariable long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody FormRequestDTO requestDto){

        // if (userDetails == null) {
        //     throw new IllegalArgumentException("로그인이 필요합니다.");
        // }
        
        String username = userDetails.getUsername();
        
        List<FormListResponseDTO> got = formApiService.getAllForms(postId, username, requestDto);

        ApiResponse<FormListResponseDTO> gotForms = ApiResponse.successList(
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
    public ResponseEntity<ApiResponse<FormResponseDTO>> getFormById(
            @PathVariable long postId,
            @PathVariable long formId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody FormRequestDTO requestDto){

        
        String username = userDetails.getUsername();
        
        FormResponseDTO got = formApiService.getFormById(postId, formId, username, requestDto);

        ApiResponse<FormResponseDTO> gotForm = ApiResponse.success(
                SuccessCode.OK,
                "신청폼이 정상적으로 조회되었습니다.",
                got);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gotForm);
    }

    // 신청폼 생성
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable long postId,
            @RequestBody FormRequestDTO requestDto){

        
        String username = userDetails.getUsername();
        
        formApiService.create(username, postId, requestDto);

        ApiResponse<Object> createdForm = ApiResponse.success(
                SuccessCode.CREATED,
                "신청폼이 성공적으로 제출되었습니다.",
                null);

        return ResponseEntity
                .status(HttpStatus.CREATED).
                body(createdForm);
    }

    @PatchMapping("/{formId}/status/{status}")
    public ResponseEntity<ApiResponse<Participant>> updateParticipant(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable long postId,
            @PathVariable long formId,
            @PathVariable String status){   // ACCEPT, REFUSE

        if (userDetails == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        String username = userDetails.getUsername();

        switch (status.toLowerCase()) {
            case "accept" -> formApiService.acceptForm(postId, formId, username);   // 신청서 수락
            case "refuse" -> formApiService.refuseForm(postId, formId, username);   // 신청서 거절
            default -> throw new IllegalArgumentException("지원하지 않는 상태입니다.");
        }

        ApiResponse<Participant> updatedFormStatus = ApiResponse.success(
                SuccessCode.OK,
                "신청서 상태가 성공적으로 변경되었습니다.",
                null
        );

        return ResponseEntity
                .status(HttpStatus.OK).
                body(updatedFormStatus);
    }

}