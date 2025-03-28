package com.itda.moamoa.domain.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LIkeRequestDTO {
    private long like_id;
    private long user;
    private long post;

    public void setPostId(long postId) {    // postId 한정 Setter
        this.post = postId;
    }
}
