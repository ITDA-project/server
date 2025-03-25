package com.itda.moamoa.domain.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter                         // getter 자동 생성
@NoArgsConstructor              // 기본 생성자 생성
@AllArgsConstructor             // 모든 필드 생성자 생성
public class LIkeRequestDTO {
    private long like_id;
    private long user;
    private long post;

    public void setPostId(long postId) {    // postId 한정 Setter
        this.post = postId;
    }
}
