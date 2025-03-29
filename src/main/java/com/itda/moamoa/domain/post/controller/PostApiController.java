package com.itda.moamoa.domain.post.controller;

import com.itda.moamoa.domain.post.dto.PostRequestDTO;
import com.itda.moamoa.domain.post.dto.PostResponseDTO;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.post.service.PostApiService;
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
@RequestMapping("/api/posts")
public class PostApiController {
    private final PostApiService postApiService;

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<ApiResponse<PostResponseDTO>> getAllPosts(@AuthenticationPrincipal CustomUserDetails userDetails){
        String username = userDetails != null ? userDetails.getUsername() : null;
        List<PostResponseDTO> got = postApiService.getAllPosts(username);

        ApiResponse<PostResponseDTO> gotPosts = ApiResponse.successList(
                SuccessCode.OK,
                "게시글이 정상적으로 조회 되었습니다.",
                got,
                got.size());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gotPosts);
    }

    // 게시글 카테고리 조회 - 목록 (최신 모임 / 주간 인기)

    // 게시글 검색 조회 - 목록

    // 게시글 개별 조회
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDTO>> getPostById(@PathVariable long postId){
        PostResponseDTO got = postApiService.getPostById(postId);

        ApiResponse<PostResponseDTO> gotPost = ApiResponse.success(
                SuccessCode.OK,
                "게시글이 정상적으로 조회되었습니다.",
                got);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gotPost);
    }

    // 게시글 생성 요청
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> create(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody PostRequestDTO requestDto) {
        String username = userDetails.getUsername();
        
        Long postId = postApiService.create(username, requestDto);

        ApiResponse<Object> createdResponse = ApiResponse.success(
                SuccessCode.CREATED,
                "게시글을 성공적으로 등록하였습니다.",
                null);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/posts/" + postId)
                .body(createdResponse);
    }

    // 게시글 수정 요청
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<Object>> update(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable long postId, @RequestBody PostRequestDTO requestDto) {
        String username = userDetails != null ? userDetails.getUsername() : null;
        Long updatedPostId = postApiService.update(username, postId, requestDto);

        ApiResponse<Object> updatedResponse = ApiResponse.success(
                SuccessCode.OK,
                "게시글이 성공적으로 수정되었습니다.",
                null);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Location", "/api/posts/" + updatedPostId)
                .body(updatedResponse);
    }

    // 게시글 삭제 요청
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Object>> delete(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable long postId, @RequestBody PostRequestDTO requestDto) {
        String username = userDetails != null ? userDetails.getUsername() : null;
        Long deletedPostId = postApiService.delete(username, postId, requestDto);

        ApiResponse<Object> deletedResponse = ApiResponse.success(
                SuccessCode.OK,
                "게시글이 성공적으로 삭제되었습니다.",
                null);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(deletedResponse);
    }
}
