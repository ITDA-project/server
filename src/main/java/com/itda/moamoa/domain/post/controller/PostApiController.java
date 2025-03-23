package com.itda.moamoa.domain.post.controller;

import com.itda.moamoa.domain.post.dto.PostRequestDTO;
import com.itda.moamoa.domain.post.dto.PostResponseDTO;
import com.itda.moamoa.domain.post.service.PostApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostApiController {
    @Autowired      // 의존성 주입 Annotation
    private PostApiService postApiService;

    // 게시글 전체 조회 -> 목록 조회

    // 게시글 개별 조회

    // 게시글 생성 요청 Annotation
    @PostMapping("/api/posts")
    public ResponseEntity<PostResponseDTO> create(@RequestBody PostRequestDTO requestDTO) {
        // 1. 게시글 생성을 Service에 위임
        PostResponseDTO created = postApiService.create(requestDTO);

        // 2. JSON 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 상태 코드 201 반환
    }

    // 게시글 숙정

    // 게시글 삭제
}
