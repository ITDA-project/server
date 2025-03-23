package com.itda.moamoa.domain.post.dto;

import com.itda.moamoa.domain.post.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter                         // getter 자동 생성 Annotation
@NoArgsConstructor              // 기본 생성자 생성 Annotation
@AllArgsConstructor             // 생성자 생성 Annotation

public class PostRequestDTO {
    private long post_id;
    private long user_id;
    private String title;
    private String content;
    private Category category;
    private Integer mambersMax;
    private String location;
    private LocalDate dueDate;
}
