package com.itda.moamoa.domain.like.controller;

import com.itda.moamoa.domain.like.service.LikeApiService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import com.itda.moamoa.global.security.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/likes")
public class LikeApiController {
    private final LikeApiService likeApiService;

    // 좋아요 생성 요청
    @PostMapping
    public ResponseEntity<ApiResponse<Object>> create(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @PathVariable long postId){

       likeApiService.create(userDetails.getUsername(), postId);

        ApiResponse<Object> createdLike = ApiResponse.success(
                SuccessCode.CREATED,
                "해당 게시글을에 좋아요를 누르셨습니다.",
                null);

        return ResponseEntity
                .status(HttpStatus.CREATED).
                body(createdLike);
    }

    // 해당 게시글 좋아요 삭제 요청
    @DeleteMapping
    public ResponseEntity<ApiResponse<Object>> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                              @PathVariable long postId){

        likeApiService.delete(userDetails.getUsername(), postId);

        ApiResponse<Object> deletedLike = ApiResponse.success(
            SuccessCode.OK,
            "해당 게시글에 좋아요를 취소하셨습니다.",
            null);

        return ResponseEntity
                .status(HttpStatus.OK).
                body(deletedLike);
    }
}
