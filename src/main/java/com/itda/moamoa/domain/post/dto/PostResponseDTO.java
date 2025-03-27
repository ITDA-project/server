package com.itda.moamoa.domain.post.dto;

import com.itda.moamoa.domain.post.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter                         // getter 자동 생성
@Setter                         // Setter 자동 생성
@NoArgsConstructor              // 기본 생성자 생성
@AllArgsConstructor             // 모든 필드 생성자 생성

public class PostResponseDTO {
    private String title;
    private String content;
    private Category category;
    private Integer membersMax;
    private String location;
    private LocalDate dueDate;
}
