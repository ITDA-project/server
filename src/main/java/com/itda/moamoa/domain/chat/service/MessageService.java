package com.itda.moamoa.domain.chat.service;

import com.itda.moamoa.domain.chat.dto.MessageRequestDto;
import com.itda.moamoa.domain.chat.entity.ChatMessage;
import com.itda.moamoa.domain.chat.entity.ChatRoom;
import com.itda.moamoa.domain.chat.repository.ChatMessageRepository;
import com.itda.moamoa.domain.chat.repository.ChatRoomRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public void saveMessage(MessageRequestDto messageRequestDto){
        ChatRoom chatRoom = chatRoomRepository.findById(messageRequestDto.getRoomId()).orElseThrow();
        User user = userRepository.findByUsername(messageRequestDto.getUsername()).orElseThrow();

        ChatMessage message = ChatMessage.builder()
                .room(chatRoom)
                .sender(user)
                .content(messageRequestDto.getContent())
                .build();

        chatMessageRepository.save(message);
    }
}
