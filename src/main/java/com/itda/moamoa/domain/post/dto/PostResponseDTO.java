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
    private LocalDate ActivityStartDate;
    private LocalDate ActivityEndDate;
    private LocalDateTime createdAt; //모집 시작 날짜도 얘로 활용
    //작성자 id

}
