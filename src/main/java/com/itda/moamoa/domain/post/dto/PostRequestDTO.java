package com.itda.moamoa.domain.post.dto;

import com.itda.moamoa.domain.post.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PostRequestDTO {
    private String title;
    private String content;
    private Category category;
    private Integer membersMax;
    private String location;
    private LocalDate dueDate;
    private String warranty;
    private LocalDate ActivityStartDate;
    private LocalDate ActivityEndDate;
    //likesCount, participantsCOunt 제외
}
