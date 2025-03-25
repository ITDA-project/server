package com.itda.moamoa.domain.form.controller;

import com.itda.moamoa.domain.form.dto.FormRequestDTO;
import com.itda.moamoa.domain.form.dto.FormResponseDTO;
import com.itda.moamoa.domain.form.entity.Form;
import com.itda.moamoa.domain.form.service.FormApiService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController             // REST API Controller
@RequiredArgsConstructor    // 필수 필드(final, @NonNull) 생성자 -> 의존성 주입 대체
public class FormApiController {
    private final FormApiService formApiService;

    // 해당 게시글에 제출된 신청폼 전체 조회
    @GetMapping("/api/posts/{postid}/form")
    public ResponseEntity<ApiResponse<Form>> getAllForms(@PathVariable long postId, @RequestBody FormRequestDTO requestDto){
        // 1. postId 중복 방지 -> RequestDTO postid = URL 내 postid
        requestDto.setPostId(postId);

        // 2. 신청폼 조회를 Service에 위임
        List<Form> geted = formApiService.getAllForms(postId, requestDto);

        // 3. API Response 생성
        ApiResponse<Form> getedForms = ApiResponse.successList(
                SuccessCode.OK,
                "신청폼 조회 어쩌구저쩌구",
                geted,
                geted.size());

        // 4. JSON 응답 반환
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(getedForms);
    }

    // 해당 게시글에 제출된 신청폼 개별 조회
    @GetMapping("/api/posts/{postid}/form/{formid}")
    public ResponseEntity<ApiResponse<Form>> getFormById(@PathVariable long postId, @PathVariable long formId, @RequestBody FormRequestDTO requestDto){
        // 1. postId 중복 방지 -> RequestDTO postid = URL 내 postid
        requestDto.setPostId(postId);

        // 2. 신청폼 조회를 Service에 위임
        Form geted = formApiService.getFormById(postId, formId, requestDto);

        // 3. API Response 생성
        ApiResponse<Form> getedForm = ApiResponse.success(
                SuccessCode.OK,
                "신청폼이 성공적으로 제출되었습니다.",
                geted);

        // 4. JSON 응답 반환
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(getedForm);
    }

    // 신청폼 생성
    @PostMapping("/api/posts/{postid}/form")     // URL 내 변수 사용, HTTP Body 내 변수 사용
    public ResponseEntity<ApiResponse<FormResponseDTO>> create(@PathVariable long postId, @RequestBody FormRequestDTO requestDto){
        // 1. Post id 중복 방지 ->  RequestDTO postId = URL postId
        requestDto.setPostId(postId);

        // 2. 신청폼 생성을 Service에 위임
        FormResponseDTO created = formApiService.create(requestDto);

        // 3. API Response 생성
        ApiResponse<FormResponseDTO> createdForm = ApiResponse.success(
                SuccessCode.CREATED,
                "신청폼이 성공적으로 제출되었습니다.",
                created);

        // 4. JSON 응답 반환
        return ResponseEntity
                .status(HttpStatus.CREATED).
                body(createdForm);   // 상태 코드 201 반환
    }
}
