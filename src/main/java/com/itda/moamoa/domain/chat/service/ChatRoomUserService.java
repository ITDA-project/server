package com.itda.moamoa.domain.chat.service;

import com.itda.moamoa.domain.chat.dto.ChatRoomParticipantsDto;
import com.itda.moamoa.domain.chat.entity.RoomRole;
import com.itda.moamoa.domain.chat.repository.ChatRoomUserRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomUserService {
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserRepository userRepository;

    public RoomRole findRoomRole(Long roomId,String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        return chatRoomUserRepository.findByUserIdAndRoomId(user.getId(),roomId)
                .orElseThrow(()->new EntityNotFoundException("존재하지 않는 데이터입니다."))
                .getRole();
    }


    public List<ChatRoomParticipantsDto> getParticipants(Long roomId){
        return chatRoomUserRepository.findByRoomId(roomId);
    }
}
