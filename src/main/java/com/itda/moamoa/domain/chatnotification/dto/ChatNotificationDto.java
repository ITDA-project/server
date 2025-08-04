package com.itda.moamoa.domain.chatnotification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatNotificationDto {
    private Long id;
    private String sender;
    private String receiver;
    private String chatRoomName;
    private Long chatMessageId;
    private ChatNotificationType chatNotificationType;
    private String content;
    private LocalDateTime createdAt;
    private boolean read;
}
