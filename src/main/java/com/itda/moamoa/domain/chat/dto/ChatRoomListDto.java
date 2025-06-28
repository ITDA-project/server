package com.itda.moamoa.domain.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 채팅방 목록 조회 응답 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class ChatRoomListDto {
    private Long id;
    private String roomName;
    private LocalDateTime lastMessageAt;
    private Long unread;

    public ChatRoomListDto(Long id, String roomName, LocalDateTime lastMessageAt, Long unread) {
        this.id = id;
        this.roomName = roomName;
        this.lastMessageAt = lastMessageAt;
        this.unread = unread;
    }
}
