package com.itda.moamoa.domain.post.controller;

import com.itda.moamoa.domain.post.dto.PostRequestDTO;
import com.itda.moamoa.domain.post.dto.PostResponseDTO;
import com.itda.moamoa.domain.post.service.PostApiService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController             // RestController
@RequiredArgsConstructor    // 필수 필드 생성자
public class PostApiController {
    private final PostApiService postApiService;

    // 게시글 전체 조회 -> 목록 조회

    // 게시글 개별 조회

    // 게시글 생성 요청
    @PostMapping("/api/posts")              // HTTP Body 내 변수 사용
    public ResponseEntity<ApiResponse<PostResponseDTO>> create(@RequestBody PostRequestDTO requestDto) {
        // 1. 게시글 생성을 Service에 위임
        PostResponseDTO created = postApiService.create(requestDto);

        // 2. ApiResponse 생성
        ApiResponse<PostResponseDTO> createdPost = ApiResponse.success(
                SuccessCode.CREATED,
                "게시글을 성공적으로 등록되셨습니다.",
                created);

        // 4. JSON 응답 반환
        return ResponseEntity
                .status(HttpStatus.CREATED).
                body(createdPost);
    }

    // 게시글 숙정

    // 게시글 삭제
}
