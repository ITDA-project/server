package com.itda.moamoa.domain.like.controller;

import com.itda.moamoa.domain.form.dto.FormResponseDTO;
import com.itda.moamoa.domain.like.dto.LIkeRequestDTO;
import com.itda.moamoa.domain.like.dto.LikeResponseDTO;
import com.itda.moamoa.domain.like.entity.Like;
import com.itda.moamoa.domain.like.service.LikeApiService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController                 // REST API Controller
@RequiredArgsConstructor        // 필수 필드 생성자
public class LikeApiController {
    private final LikeApiService likeApiService;

    // 해당 게시글의 전체 좋아요 수 조회 요청
//    @GetMapping("/posts/{postId}/likes")
//    public ResponseEntity<ApiResponse<Like>> getLike(@PathVariable long postid, @RequestBody LIkeRequestDTO requestDto){
//        // 1. Post_id 중복 방지 -> RequestDTO Post id = URL Post id
//        requestDto.setPostId(postid);
//
//        // 2. 좋아요 조회를 Service에 위임
//        Like geted = likeApiService.getLike(postid, requestDto);
//
//        // 3. ApiResponse 생성
//        ApiResponse<Like> getedLike = ApiResponse.success(
//                SuccessCode.OK,
//                "해당 게시글의 좋아요가 성공적으로 조회되었습니다.",
//                geted);
//
//        // 4. JSON 응답 반환
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(getedLike);
//    }


    // 좋아요 생성 요청
    @PostMapping("/api/posts/{postId}/likes")       // URL 변수 사용, HTTP Body 변수 사용
    public ResponseEntity<ApiResponse<LikeResponseDTO>> create(@PathVariable long postId, @RequestBody LIkeRequestDTO requestDto){
        // 1. Post_id 중복 방지 -> RequestDTO Post id = URL Post id
        requestDto.setPostId(postId);

        // 2. 좋아요 생성을 Service에 위임
        LikeResponseDTO created = likeApiService.create(requestDto);

        // 3. ApiResponse 생성
        ApiResponse<LikeResponseDTO> createdLike = ApiResponse.success(
                SuccessCode.CREATED,
                "해당 게시글을에 좋아요를 누르셨습니다.",
                created);

        // 4. JSON 응답 반환
        return ResponseEntity
                .status(HttpStatus.CREATED).
                body(createdLike);   // 상태 코드 201 반환
    }

    // 좋아요 삭제 요청
//    @DeleteMapping("/likes/{likeId} ")
//    public ResponseEntity<LikeResponseDTO> delete(@PathVariable long likeId, @RequestBody LIkeRequestDTO requestDto){
//        // 1. 좋아요 삭제를 Service에 위임
//        LikeResponseDTO deleted = likeApiService.delete(requestDto);
//
//        // 2. 생성된 ResponseDTO 반환
//        return ;
//    }
}
