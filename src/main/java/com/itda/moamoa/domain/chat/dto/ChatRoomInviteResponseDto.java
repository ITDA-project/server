package com.itda.moamoa.domain.chat.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatRoomInviteResponseDto {
    private String roomName;
    private Long id;
}
