package com.itda.moamoa.domain.chat.controller;

import com.itda.moamoa.domain.chat.dto.MessageRequestDto;
import com.itda.moamoa.domain.chat.dto.MessageResponseDto;
import com.itda.moamoa.domain.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    //응답:메시지 내용, 메시지 보낸 사람, 전송 시간
    @MessageMapping("/{roomId}")
    public void sendMessage(@DestinationVariable("roomId") Long roomId, Principal user, @Payload MessageRequestDto messageRequestDto){
        messageRequestDto.setUsername(user.getName());
        messageRequestDto.setRoomId(roomId);
        //service -> 보낸 메시지 내용을 저장
        MessageResponseDto messageResponseDto = messageService.saveMessage(messageRequestDto);
        //특정 채팅방에 메시지 DTO 를 전달
        messagingTemplate.convertAndSend("/topic/room/"+roomId,messageResponseDto);
    }
}
