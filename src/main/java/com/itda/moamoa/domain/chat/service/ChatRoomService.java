package com.itda.moamoa.domain.chat.service;

import com.itda.moamoa.domain.chat.dto.ChatRoomInviteRequestDto;
import com.itda.moamoa.domain.chat.dto.ChatRoomListDto;
import com.itda.moamoa.domain.chat.dto.ChatRoomMessageResponseDto;
import com.itda.moamoa.domain.chat.entity.ChatRoom;
import com.itda.moamoa.domain.chat.entity.ChatRoomUser;
import com.itda.moamoa.domain.chat.entity.RoomRole;
import com.itda.moamoa.domain.chat.repository.ChatMessageRepository;
import com.itda.moamoa.domain.chat.repository.ChatRoomRepository;
import com.itda.moamoa.domain.chat.repository.ChatRoomUserRepository;
import com.itda.moamoa.domain.post.entity.Post;
import com.itda.moamoa.domain.post.repository.PostRepository;
import com.itda.moamoa.domain.user.entity.User;
import com.itda.moamoa.domain.user.repository.UserRepository;
import com.itda.moamoa.global.exception.custom.UserException;
import com.itda.moamoa.global.security.jwt.dto.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final PostRepository postRepository;

    public Long createChatRoom(String roomName, CustomUserDetails userDetails){
        ChatRoom chatRoom = ChatRoom.builder().
                roomName(roomName).
                build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        //ChatRoomUser 에 방장 저장
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new EntityNotFoundException("찾을 수 없는 유저입니다"));

        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .user(user)
                .room(savedChatRoom)
                .role(RoomRole.OWNER)
                .build();
        chatRoomUserRepository.save(chatRoomUser);
        return savedChatRoom.getId();
    }

    public void leaveChatRoom(Long roomId,CustomUserDetails userDetails){
        //userId 가져오기
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new EntityNotFoundException("찾을 수 없는 유저입니다"));;

        //OWNER 라면 채팅방 deleteFlag 수정
        ChatRoomUser leaveRoomUser = chatRoomUserRepository.findByUserIdAndRoomId(user.getId(),roomId).orElseThrow(() -> new EntityNotFoundException("찾을 수 없는 유저입니다"));
        if(RoomRole.OWNER.equals(leaveRoomUser.getRole())){
            ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow();
            chatRoom.softDelete();
        }
        //ChatRoomUser 에서 삭제
        chatRoomUserRepository.delete(leaveRoomUser);
    }

    //사용자가 첫 채팅방을 들어왔을 때와 그 다음 예전 메시지를 불러오는 로직 구분 해야 함
    public List<ChatRoomMessageResponseDto> getRoomChatting(Long roomId, Long cursor, int size){
        //페이징 사용
        if (cursor == null || cursor <= 0) {
            cursor = Long.MAX_VALUE;
        }
        Pageable pageable = PageRequest.of(0,size, Sort.by(Sort.Direction.DESC,"id"));

        //최신 순으로 정렬해서 데이터를 반환한다.
        return chatMessageRepository.findByRoomIdAndIdLessThanOrderByIdDesc(roomId, cursor, pageable)
                .stream()
                .map(ChatRoomMessageResponseDto::fromMessage)
                .toList();
    }

    public ChatRoom inviteUser(ChatRoomInviteRequestDto chatRoomInviteRequestDto){
        //초대한 유저가 탈퇴한 경우
        User user = userRepository.findByUsername(chatRoomInviteRequestDto.getUsername()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        if(user.isDeleted()){
            throw new UserException("이미 탈퇴한 회원입니다.");
        }
        Post post = postRepository.findById(chatRoomInviteRequestDto.getPostId()).orElseThrow(()->new EntityNotFoundException("존재하지 않는 게시글입니다."));
        ChatRoom chatRoom = post.getChatRoom();

        ChatRoomUser invitedUser = ChatRoomUser.builder()
                .room(chatRoom)
                .user(user)
                .role(RoomRole.USER)
                .build();
        chatRoomUserRepository.save(invitedUser);
        return chatRoom;
    }

    public List<ChatRoomListDto> getChatRoomList(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        return chatRoomRepository.findChatRoomList(user.getId());
    }

    public Boolean isDeleted(Long roomId){
        return chatRoomRepository
                .findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 채팅방입니다."))
                .isDeleted();
    }

}
