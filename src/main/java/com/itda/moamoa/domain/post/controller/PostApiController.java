package com.itda.moamoa.domain.post.controller;

import com.itda.moamoa.domain.post.dto.PostRequestDTO;
import com.itda.moamoa.domain.post.dto.PostResponseDTO;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.post.service.PostApiService;
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
@RequestMapping("/api/posts")
public class PostApiController {
    private final PostApiService postApiService;

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<ApiResponse<Post>> getAllPosts(@AuthenticationPrincipal String username){
        List<Post> got = postApiService.getAllPosts(username);

        ApiResponse<Post> gotPosts = ApiResponse.successList(
                SuccessCode.OK,
                "신청폼이 정상적으로 조회 되었습니다.",
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
    public ResponseEntity<ApiResponse<Post>> getPostById(@PathVariable long postId, @AuthenticationPrincipal String username){
        Post got = postApiService.getPostById(postId, username);

        ApiResponse<Post> gotPost = ApiResponse.success(
                SuccessCode.OK,
                ".",
                got);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gotPost);
    }

    // 게시글 생성 요청
    @PostMapping
    public ResponseEntity<ApiResponse<PostResponseDTO>> create(@AuthenticationPrincipal String username, @RequestBody PostRequestDTO requestDto) {
        PostResponseDTO created = postApiService.create(username, requestDto);

        ApiResponse<PostResponseDTO> createdPost = ApiResponse.success(
                SuccessCode.CREATED,
                "게시글을 성공적으로 등록되셨습니다.",
                created);

        return ResponseEntity
                .status(HttpStatus.CREATED).
                body(createdPost);
    }

    // 게시글 수정 요청
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDTO>> update(@AuthenticationPrincipal String username, @PathVariable long postId, @RequestBody PostRequestDTO requestDto) {
        PostResponseDTO updated = postApiService.update(username, postId, requestDto);

        ApiResponse<PostResponseDTO> updatedPost = ApiResponse.success(
                SuccessCode.OK,
                "게시글이 성공적으로 수정되셨습니다.",
                updated);

        return ResponseEntity
                .status(HttpStatus.OK).
                body(updatedPost);
    }

    // 게시글 삭제 요청
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDTO>> delete(@AuthenticationPrincipal String username, @PathVariable long postId, @RequestBody PostRequestDTO requestDto) {
        PostResponseDTO deleted = postApiService.delete(username, postId, requestDto);

        ApiResponse<PostResponseDTO> deletedPost = ApiResponse.success(
                SuccessCode.OK,
                "게시글이 성공적으로 삭제되셨습니다.",
                deleted);

        return ResponseEntity
                .status(HttpStatus.OK).
                body(deletedPost);
    }
}
