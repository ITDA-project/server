package com.itda.moamoa.domain.spec.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MyPageDTO {
    private String name;             // 사용자 이름
    private String image;          // 프로필 이미지 URL
    private Double ratingAverage;    // 평균 별점
    private List<PostDTO> joinedPosts;   // 신청한 모임 목록
    private List<PostDTO> likedPosts;    // 좋아요한 모임 목록 
    private List<PostDTO> createdPosts;  // 생성한 모임 목록
    
    @Getter
    @Builder
    public static class PostDTO {
        private Long id;             // 게시글 ID
        private String title;        // 게시글 제목
        private LocalDateTime createdAt;  // 생성일
        private Long userId;         // 작성자 ID
    }
} 