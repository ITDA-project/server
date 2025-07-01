package com.itda.moamoa.domain.chat.controller;

import com.itda.moamoa.domain.chat.dto.*;
import com.itda.moamoa.domain.chat.entity.ChatRoom;
import com.itda.moamoa.domain.chat.entity.RoomRole;
import com.itda.moamoa.domain.chat.service.ChatRoomService;
import com.itda.moamoa.domain.chat.service.ChatRoomUserService;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import com.itda.moamoa.global.security.jwt.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatroom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatRoomUserService chatRoomUserService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ModelMapper modelMapper;

    //채팅방 생성
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createChatRoom(@RequestBody ChatRoomDto roomName, @AuthenticationPrincipal CustomUserDetails user){
        Long chatRoomId = chatRoomService.createChatRoom(roomName.getRoomName(), user);

        //채팅방 아이디를 알려주면, 해당 경로를 SUBSCRIBE
        ApiResponse<Long> apiResponse = ApiResponse.success(SuccessCode.CREATED,"채팅방 생성 완료",chatRoomId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    //채팅방 초대
    //오프라인은 채팅방 DB에 초대받은 사용자를 추가해서 조회할 때, 가져오면 됨
    //유저 식별은 username 으로 구분함, form 수락 시 username 도 보내줘야 됨
    //초대한 사용자, ChatRoomUser 에 넣기
    //초대한 사용자에게 채팅방 정보를 넘겨줌
    @PostMapping("/invite")
    public ResponseEntity<ApiResponse<Object>> inviteUser(@RequestBody ChatRoomInviteRequestDto chatRoomInviteRequestDto){
        ChatRoom chatRoom = chatRoomService.inviteUser(chatRoomInviteRequestDto);

        ChatRoomInviteResponseDto chatRoomDto = modelMapper.map(chatRoom, ChatRoomInviteResponseDto.class);
        messagingTemplate.convertAndSendToUser(chatRoomInviteRequestDto.getUsername(),"/queue/invite",chatRoomDto);

        ApiResponse<Object> apiResponse = ApiResponse.success(SuccessCode.OK,"초대 성공",null);
        return ResponseEntity.ok(apiResponse);
    }

    //채팅방 퇴장
    @DeleteMapping("/{roomId}")
    public ResponseEntity<ApiResponse<Object>> leaveChatRoom(@PathVariable(name = "roomId") Long roomId,@AuthenticationPrincipal CustomUserDetails user) {
        chatRoomService.leaveChatRoom(roomId,user);

        //퇴장 후에는 클라이언트에서 UNSUBSCRIBE 요청 필요
        ApiResponse<Object> apiResponse = ApiResponse.success(SuccessCode.OK,"채팅방 퇴장 완료",null);
        return ResponseEntity.ok(apiResponse);
    }

    //채팅방 목록 조회
    //roomName, roomId, lastMessage, 읽지 않은 메시지 수
    //마지막 메시지의 내용은 /user/queue/last-message 를 통해 전달해서 실시간 갱신?
    @GetMapping
    public ResponseEntity<ApiResponse<ChatRoomListDto>> getChatRoom(@AuthenticationPrincipal CustomUserDetails user) {
        List<ChatRoomListDto> chatRoomList = chatRoomService.getChatRoomList(user.getUsername());

        ApiResponse<ChatRoomListDto> response = ApiResponse.successList(SuccessCode.OK,"채팅방 목록 조회 완료",chatRoomList,chatRoomList.size());
        return ResponseEntity.ok(response);
    }

    //이전 채팅들 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<Map<String,Object>>> getChatOfChatRoom(@PathVariable(name = "roomId") Long roomId,
                                                                                           @RequestParam(required = false)Long cursor,
                                                                                           @RequestParam(defaultValue = "30")int size,
                                                                                     @AuthenticationPrincipal CustomUserDetails user){
        List<ChatRoomMessageResponseDto> messages = chatRoomService.getRoomChatting(roomId, cursor, size);
        Map<String,Object> response = new HashMap<>();
        response.put("messages",messages);
        response.put("size",messages.size());
        if(cursor == null || cursor <= 0){
            RoomRole roomRole = chatRoomUserService.findRoomRole(roomId, user.getUsername());
            Boolean deleteFlag = chatRoomService.isDeleted(roomId);
            response.put("role",roomRole);
            response.put("deleteFlag",deleteFlag);
        }

        ApiResponse<Map<String,Object>> apiResponse = ApiResponse.success(SuccessCode.OK, "채팅방 채팅 조회 완료", response);
        return ResponseEntity.ok(apiResponse);
    }
}
