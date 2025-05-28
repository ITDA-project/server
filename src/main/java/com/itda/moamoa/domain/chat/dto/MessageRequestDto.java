package com.itda.moamoa.domain.chat.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 메시지 전송 요청 DTO
 */
@Getter
@Setter
public class MessageRequestDto {
    private String content;
    private Long roomId;
    private String username;
}
