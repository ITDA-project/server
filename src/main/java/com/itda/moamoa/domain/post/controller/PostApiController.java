package com.itda.moamoa.domain.post.controller;

import com.itda.moamoa.domain.post.dto.PostRequestDTO;
import com.itda.moamoa.domain.post.dto.PostResponseDTO;
import com.itda.moamoa.domain.post.dto.PostListResponseDTO;
import com.itda.moamoa.domain.post.entity.Category;
import com.itda.moamoa.domain.post.service.PostApiService;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    // 게시글 전체 조회
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<PostListResponseDTO>> getPostList(
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false) Category category,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "10") int size) {
        
        List<PostListResponseDTO> posts = postApiService.getPostsByCursor(cursor, category, sort, size);
        
        ApiResponse<PostListResponseDTO> response = ApiResponse.successList(
                SuccessCode.OK,
                "게시글 목록이 정상적으로 조회되었습니다.",
                posts,
                posts.size());
                
        return ResponseEntity.ok(response);
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
    public ResponseEntity<ApiResponse<Object>> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody PostRequestDTO requestDto) {

        if (userDetails == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }
        
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
    public ResponseEntity<ApiResponse<Object>> update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable long postId,
            @RequestBody PostRequestDTO requestDto) {

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
    public ResponseEntity<ApiResponse<Object>> delete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable long postId) {

        String username = userDetails != null ? userDetails.getUsername() : null;
        Long deletedPostId = postApiService.delete(username, postId);

        ApiResponse<Object> deletedResponse = ApiResponse.success(
                SuccessCode.OK,
                "게시글이 성공적으로 삭제되었습니다.",
                null);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(deletedResponse);
    }

    // 내가 쓴 글 목록 조회
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<PostListResponseDTO>> getMyPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size) {

        if (userDetails == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        List<PostListResponseDTO> posts = postApiService.getPostsByUserId(user.getId(), cursor, size);

        ApiResponse<PostListResponseDTO> response = ApiResponse.successList(
                SuccessCode.OK,
                "내가 작성한 게시글 목록이 정상적으로 조회되었습니다.",
                posts,
                posts.size());

        return ResponseEntity.ok(response);
    }

   // 내가 좋아요한 글 목록 조회
    @GetMapping("/liked")
    public ResponseEntity<ApiResponse<PostListResponseDTO>> getLikedPosts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size) {

        if (userDetails == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // TODO: 좋아요 기능 구현 후 테스트
        List<PostListResponseDTO> posts = postApiService.getLikedPostsByUserId(user.getId(), cursor, size);

        ApiResponse<PostListResponseDTO> response = ApiResponse.successList(
                SuccessCode.OK,
                "내가 좋아요한 게시글 목록이 정상적으로 조회되었습니다.",
                posts,
                posts.size());

        return ResponseEntity.ok(response);
    }
}
