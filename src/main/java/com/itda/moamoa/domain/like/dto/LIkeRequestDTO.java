package com.itda.moamoa.domain.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter                         // getter 자동 생성 Annotation
@NoArgsConstructor              // 기본 생성자 생성 Annotation
@AllArgsConstructor             // 생성자 생성 Annotation
public class LIkeRequestDTO {
    private long like_id;
    private long user_id;
    private long post_id;

    public void setPostId(long postId) {    // postId 한정 Setter
        this.post_id = postId;
    }
}
