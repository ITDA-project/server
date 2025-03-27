package com.itda.moamoa.domain.post.controller;

import com.itda.moamoa.domain.form.dto.FormRequestDTO;
import com.itda.moamoa.domain.form.entity.Form;
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

@RestController             // RestController
@RequiredArgsConstructor        // 필수 필드 생성자
@RequestMapping("/api/posts")
public class PostApiController {
    private final PostApiService postApiService;
    // 권한 없는 사용자도 게시글 조회 가능

    // 게시글 전체 조회 -> 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<Post>> getAllPosts(@AuthenticationPrincipal String username){
        // 1. 신청폼 조회를 Service 위임
        List<Post> got = postApiService.getAllPosts(username);

        // 2. API Response 생성
        ApiResponse<Post> gotPosts = ApiResponse.successList(
                SuccessCode.OK,
                "신청폼이 정상적으로 조회 되었습니다.",
                got,
                got.size());

        // 3. JSON 응답 반환
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gotPosts);    // 상태 코드 200 반환
    }

    // 게시글 개별 조회
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<Post>> getPostById(@PathVariable long postId, @AuthenticationPrincipal String username){
        // 1. 신청폼 조회를 Service 위임
        Post got = postApiService.getPostById(postId, username);

        // 2. API Response 생성
        ApiResponse<Post> gotPost = ApiResponse.success(
                SuccessCode.OK,
                ".",
                got);

        // 3. JSON 응답 반환
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(gotPost);     // 상태 코드 200 반환
    }

    // 게시글 생성 요청
    @PostMapping              // Token username 사용, URL 내 변수 사용, HTTP Body 내 변수 사용
    public ResponseEntity<ApiResponse<PostResponseDTO>> create(@AuthenticationPrincipal String username, @RequestBody PostRequestDTO requestDto) {
        // 1. 게시글 생성을 Service 위임
        PostResponseDTO created = postApiService.create(username, requestDto);

        // 2. ApiResponse 생성
        ApiResponse<PostResponseDTO> createdPost = ApiResponse.success(
                SuccessCode.CREATED,
                "게시글을 성공적으로 등록되셨습니다.",
                created);

        // 4. JSON 응답 반환
        return ResponseEntity
                .status(HttpStatus.CREATED).
                body(createdPost);          // 상태 코드 201 전송
    }

    // 게시글 수정 요청
    @PatchMapping("/{postId}")              // Token 내 username 사용, URL 내 변수 사용, HTTP Body 내 변수 사용
    public ResponseEntity<ApiResponse<PostResponseDTO>> update(@AuthenticationPrincipal String username, @PathVariable long postId, @RequestBody PostRequestDTO requestDto) {
        // 1. 게시글 생성을 Service 위임
        PostResponseDTO updated = postApiService.update(username, postId, requestDto);

        // 2. ApiResponse 생성
        ApiResponse<PostResponseDTO> updatedPost = ApiResponse.success(
                SuccessCode.OK,
                "게시글이 성공적으로 수정되셨습니다.",
                updated);

        // 3. JSON 응답 반환
        return ResponseEntity
                .status(HttpStatus.OK).
                body(updatedPost);          // 상태 코드 200 전송
    }

    // 게시글 삭제 요청
    @DeleteMapping("/api/posts/{postId}")           // Token 내 username 사용, URL 내 변수 사용, HTTP Body 내 변수 사용
    public ResponseEntity<ApiResponse<PostResponseDTO>> delete(@AuthenticationPrincipal String username, @PathVariable long postId, @RequestBody PostRequestDTO requestDto) {
        // 1. 게시글 생성을 Service 위임
        PostResponseDTO deleted = postApiService.delete(username, postId, requestDto);

        // 2. ApiResponse 생성
        ApiResponse<PostResponseDTO> deletedPost = ApiResponse.success(
                SuccessCode.OK,
                "게시글이 성공적으로 삭제되셨습니다.",
                deleted);

        // 4. JSON 응답 반환
        return ResponseEntity
                .status(HttpStatus.OK).
                body(deletedPost);          // 상태 코드 200 전송
    }
}
