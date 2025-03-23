package com.itda.moamoa.domain.form.controller;

import com.itda.moamoa.domain.form.dto.FormRequestDTO;
import com.itda.moamoa.domain.form.dto.FormResponseDTO;
import com.itda.moamoa.domain.form.service.FormApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController     // REST API Controller Annotation
public class FormApiController {
    @Autowired      // 의존성 주입 Annotation
    private FormApiService formApiService;

    // 신청폼 전체 조회

    // 신청폼 개별 조회

    // 신청폼 생성
    @PostMapping("/api/posts/{postid}/form")     // URL 내 변수 사용 Annotation, HTTP Body 내 변수 사용 Annotation
    public ResponseEntity<FormResponseDTO> create(@PathVariable long postId, @RequestBody FormRequestDTO requestDto){
        // 1. Post id 중복 방지 ->  RequestDTO postId = URL postId
        requestDto.setPostId(postId);

        // 2. 신청폼 생성을 Service에 위임
        FormResponseDTO created = formApiService.create(requestDto);

        // 3. JSON 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(created);   // 상태 코드 201 반환
    }
}
