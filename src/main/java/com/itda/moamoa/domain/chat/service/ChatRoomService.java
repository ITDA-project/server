package com.itda.moamoa.domain.chat.service;

import com.itda.moamoa.domain.chat.entity.ChatRoom;
import com.itda.moamoa.domain.chat.entity.ChatRoomUser;
import com.itda.moamoa.domain.chat.entity.RoomRole;
import com.itda.moamoa.domain.chat.repository.ChatRoomRepository;
import com.itda.moamoa.domain.chat.repository.ChatRoomUserRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import com.itda.moamoa.global.security.jwt.dto.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    public void createChatRoom(String roomName, CustomUserDetails userDetails){
        ChatRoom chatRoom = ChatRoom.builder().
                roomName(roomName).
                build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        //ChatRoomUser 에 방장 저장
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new EntityNotFoundException("찾을 수 없는 유저입니다"));

        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .userId(user)
                .roomId(savedChatRoom)
                .role(RoomRole.OWNER)
                .build();
        chatRoomUserRepository.save(chatRoomUser);
    }

    public void leaveChatRoom(Long roomId,CustomUserDetails userDetails){
        //userId 가져오기
        Long userId = userDetails.getUserId();
        //OWNER 라면 채팅방 deleteFlag 수정
        ChatRoomUser leaveRoomUser = chatRoomUserRepository.findById(userId).orElseThrow();
        if(RoomRole.OWNER.equals(leaveRoomUser.getRole())){
            ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow();
            chatRoom.softDelete();
        }
        //ChatRoomUser 에서 해당 userId와 roomId를 삭제
        chatRoomUserRepository.deleteByUserIdAndRoomId(roomId,userId);
    }


}
