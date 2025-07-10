package com.itda.moamoa.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomParticipantsDto {
    private Long userId;
    private String participantName;
    private String image;
}
