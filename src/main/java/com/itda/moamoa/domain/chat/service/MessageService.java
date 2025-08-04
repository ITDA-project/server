package com.itda.moamoa.domain.chat.service;

import com.itda.moamoa.domain.chat.dto.MessageRequestDto;
import com.itda.moamoa.domain.chat.dto.MessageResponseDto;
import com.itda.moamoa.domain.chat.entity.ChatMessage;
import com.itda.moamoa.domain.chat.entity.ChatRoom;
import com.itda.moamoa.domain.chat.entity.ChatRoomUser;
import com.itda.moamoa.domain.chat.repository.ChatMessageRepository;
import com.itda.moamoa.domain.chat.repository.ChatRoomRepository;
import com.itda.moamoa.domain.chat.repository.ChatRoomUserRepository;
import com.itda.moamoa.domain.chatnotification.dto.ChatNotificationType;
import com.itda.moamoa.domain.chatnotification.dto.CreateChatNotificationDto;
import com.itda.moamoa.domain.chatnotification.service.ChatNotificationService;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatNotificationService chatNotificationService;

    public MessageResponseDto saveMessage(MessageRequestDto messageRequestDto){
        ChatRoom chatRoom = chatRoomRepository.findById(messageRequestDto.getRoomId()).orElseThrow();
        User user = userRepository.findByUsername(messageRequestDto.getUsername()).orElseThrow();

        ChatMessage message = ChatMessage.builder()
                .room(chatRoom)
                .sender(user)
                .content(messageRequestDto.getContent())
                .build();
        chatRoom.updateLastMessage(message.getContent(),message.getCreatedAt());
        chatMessageRepository.save(message);

        chatNotificationService.notifyChatToUser(
                CreateChatNotificationDto.builder()
                        .chatRoomName(chatRoom.getRoomName())
                        .chatMessageId(message.getId())
                        .chatNotificationType(ChatNotificationType.CHAT)
                        .sender(user.getUsername())
                        .message(message.getContent())
                        .receivers(findReceiverUsernames(chatRoom.getId(), user.getUsername()))
                        .build()
        );


        return new MessageResponseDto(message.getId(),
                user.getId(),
                user.getName(),
                user.getImage(),
                message.getContent(),
                message.getCreatedAt());
    }


    public List<String> findReceiverUsernames(Long roomId, String senderUsername){
        User sender = userRepository.findByUsername(senderUsername).orElseThrow();

        return chatRoomUserRepository.findAllByRoomId(roomId).stream()
                .map(ChatRoomUser::getUser)

                .filter(user -> !user.equals(sender))
                .map(User::getUsername)
                .collect(Collectors.toList());
    }
}
