package com.itda.moamoa.domain.chat.controller;

import com.itda.moamoa.domain.chat.service.ChatRoomService;
import com.itda.moamoa.global.security.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatroom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    //채팅방 생성
    @PostMapping
    public void createChatRoom(@RequestBody String roomName, @AuthenticationPrincipal CustomUserDetails user){
        chatRoomService.createChatRoom(roomName,user);
    }

    //채팅방 초대
    //오프라인은 채팅방 DB에 초대받은 사용자를 추가해서 조회할 때, 가져오면 됨
    //온라인 사용자는?
    //오프라인과 온라인 구별 방법

    //채팅방 퇴장(참여자)
    @DeleteMapping("/{roomId}")
    public void leaveChatRoom(@PathVariable(name = "roomId") Long roomId) {

    }

    //채팅방 제거 (방장이 폭파)

    //채팅방 목록 조회
    @GetMapping
    public void getChatRoom() {

    }

    //이전 채팅들 조회
    @GetMapping("/{roomId}")
    public void getChatOfChatRoom(){

    }
}
