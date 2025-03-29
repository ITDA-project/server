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
    private String title;
    private String content;
    private Category category;
    private Integer likesCount;
    private Integer participantCount;
    private Integer membersMax;
    private String location;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
}
