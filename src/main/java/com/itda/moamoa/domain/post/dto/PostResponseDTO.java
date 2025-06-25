package com.itda.moamoa.domain.post.dto;

import com.itda.moamoa.domain.post.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PostResponseDTO {
    private Long id; //postId
    private String title;
    private String content;
    private Category category;
    private Integer likesCount;
    private Integer participantCount;
    private Integer membersMax;
    private String location;
    private LocalDate dueDate;
    private String warranty;
    private LocalDate activityStartDate;
    private LocalDate activityEndDate;
    private LocalDateTime createdAt; //모집 시작 날짜도 얘로 활용
    private Long userId; //작성자 id
    private String userName; //작성자 이름
    private String userImage; //작성자 이미지
    private String userCareer; //작성자 경력
    private Boolean liked = false; // 현재 사용자가 좋아요했는지 여부
}
