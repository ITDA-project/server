package com.itda.moamoa.domain.like.controller;

import com.itda.moamoa.domain.like.dto.LIkeRequestDTO;
import com.itda.moamoa.domain.like.dto.LikeResponseDTO;
import com.itda.moamoa.domain.like.service.LikeApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController                 // REST API Controller Annotation
public class LikeApiController {
    @Autowired                  // 의존성 주입 Annotation
    private LikeApiService likeApiService;

    // 좋아요 수 조회 요청
    //@GetMapping("/posts/{postId}/likes")


    // 좋아요 생성 요청
    @PostMapping("/api/posts/{postId}/likes")       // URL 변수 사용 Annotation, HTTP Body 변수 사용 Annotation
    public ResponseEntity<LikeResponseDTO> create(@PathVariable long postId, @RequestBody LIkeRequestDTO requestDto){
        // Post_id 중복 방지 -> RequestDTO Post id = URL Post id
        requestDto.setPostId(postId);

        // 좋아요 생성을 Service에 위임
        LikeResponseDTO created = likeApiService.create(requestDto);

        // JSON 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(created); // 상태 코드 201 반환
    }

    // 좋아요 삭제 요청 Annotation
//    @DeleteMapping("/likes/{likeId} ")
//    public ResponseEntity<LikeResponseDTO> delete(@PathVariable long likeId, @RequestBody LIkeRequestDTO requestDto){
//        // 좋아요 삭제를 Service에 위임
//        LikeResponseDTO deleted = likeService.delete(requestDto);
//
//        // 생성된 ResponseDTO 반환
//        return (deleted != null)?
//                ResponseEntity.status(HttpStatus.OK).build() :              // 상태 코드 200 반환
//                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();      // 상태 코드 404 반환
//    }

}
