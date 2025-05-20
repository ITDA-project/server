package com.itda.moamoa.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequestDto {
    private String content;
    private Long roomId;
    private String username;
}
