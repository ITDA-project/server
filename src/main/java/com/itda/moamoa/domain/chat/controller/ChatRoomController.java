package com.itda.moamoa.domain.chat.controller;

import com.itda.moamoa.domain.chat.dto.ChatRoomCreateDto;
import com.itda.moamoa.domain.chat.dto.ChatRoomMessageResponseDto;
import com.itda.moamoa.domain.chat.service.ChatRoomService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import com.itda.moamoa.global.security.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatroom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    //채팅방 생성
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createChatRoom(@RequestBody ChatRoomCreateDto roomName, @AuthenticationPrincipal CustomUserDetails user){
        Long chatRoomId = chatRoomService.createChatRoom(roomName.getRoomName(), user);

        //채팅방 아이디를 알려주면, 해당 경로를 SUBSCRIBE
        ApiResponse<Long> apiResponse = ApiResponse.success(SuccessCode.CREATED,"채팅방 생성 완료",chatRoomId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    //채팅방 초대
    //오프라인은 채팅방 DB에 초대받은 사용자를 추가해서 조회할 때, 가져오면 됨
    //온라인 사용자는?
    //오프라인과 온라인 구별 방법

    //채팅방 퇴장
    @DeleteMapping("/{roomId}")
    public ResponseEntity<ApiResponse<Object>> leaveChatRoom(@PathVariable(name = "roomId") Long roomId,@AuthenticationPrincipal CustomUserDetails user) {
        chatRoomService.leaveChatRoom(roomId,user);

        //퇴장 후에는 클라이언트에서 UNSUBSCRIBE 요청 필요
        ApiResponse<Object> apiResponse = ApiResponse.success(SuccessCode.OK,"채팅방 퇴장 완료",null);
        return ResponseEntity.ok(apiResponse);
    }

    //채팅방 목록 조회
    @GetMapping
    public void getChatRoom() {

    }

    //이전 채팅들 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<ChatRoomMessageResponseDto>> getChatOfChatRoom(@PathVariable(name = "roomId") Long roomId,
                                                                                           @RequestParam(required = false)Long cursor,
                                                                                           @RequestParam(defaultValue = "30")int size){
        List<ChatRoomMessageResponseDto> messages = chatRoomService.getRoomChatting(roomId, cursor, size);

        ApiResponse<ChatRoomMessageResponseDto> apiResponse = ApiResponse.successList(SuccessCode.OK, "채팅방 채팅 조회 완료", messages,messages.size());
        return ResponseEntity.ok(apiResponse);
    }
}
