package com.itda.moamoa.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageResponseDto {
    private String sender;
    private String content;
    private LocalDateTime createdAt;

}
