package com.itda.moamoa.domain.chat.dto;

import lombok.*;

/**
 * 채팅방 초대 응답 DTO
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatRoomInviteResponseDto {
    private String roomName;
    private Long id;
}
