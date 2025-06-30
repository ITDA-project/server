package com.itda.moamoa.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 메시지 요청 응답 DTO
 */
@Getter
@Setter
@AllArgsConstructor
public class MessageResponseDto {
    private Long id;
    private Long senderId;
    private String senderName;
    private String profileImage;
    private String content;
    private LocalDateTime sentAt;

}
