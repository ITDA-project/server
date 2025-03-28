package com.itda.moamoa.domain.like.controller;

import com.itda.moamoa.domain.like.dto.LIkeRequestDTO;
import com.itda.moamoa.domain.like.dto.LikeResponseDTO;
import com.itda.moamoa.domain.like.service.LikeApiService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikeApiController {
    private final LikeApiService likeApiService;

    // 해당 게시글의 전체 좋아요 수 조회 요청
    @GetMapping("/api/posts/{postId}/likes")
    public ResponseEntity<ApiResponse<Integer>> getLikeCount(@PathVariable long postId, @RequestBody LIkeRequestDTO requestDto){
        Integer likeCount = likeApiService.getLikeCount(postId);

        ApiResponse<Integer> gotLike = ApiResponse.success(
                SuccessCode.OK,
                "해당 게시글의 좋아요가 성공적으로 조회되었습니다.",
                likeCount);

        return ResponseEntity
                .status(HttpStatus.CREATED).
                body(gotLike);
    }


    // 좋아요 생성 요청
    @PostMapping("/api/posts/{postId}/likes")
    public ResponseEntity<ApiResponse<LikeResponseDTO>> create(@RequestBody String username,@PathVariable long postId, @RequestBody LIkeRequestDTO requestDto){
        LikeResponseDTO created = likeApiService.create(username, postId, requestDto);

        ApiResponse<LikeResponseDTO> createdLike = ApiResponse.success(
                SuccessCode.CREATED,
                "해당 게시글을에 좋아요를 누르셨습니다.",
                created);

        return ResponseEntity
                .status(HttpStatus.CREATED).
                body(createdLike);
    }

    // 해당 게시글 좋아요 삭제 요청
    @DeleteMapping("/likes/{likeId} ")
    public ResponseEntity<ApiResponse<LikeResponseDTO>> delete(@RequestBody String username, @PathVariable long likeId, @RequestBody LIkeRequestDTO requestDto){
        LikeResponseDTO deleted = likeApiService.delete(username, likeId, requestDto);

        ApiResponse<LikeResponseDTO> deletedLike = ApiResponse.success(
            SuccessCode.OK,
            "해당 게시글에 좋아요를 취소하셨습니다.",
            deleted);

        return ResponseEntity
                .status(HttpStatus.OK).
                body(deletedLike);
    }
}
