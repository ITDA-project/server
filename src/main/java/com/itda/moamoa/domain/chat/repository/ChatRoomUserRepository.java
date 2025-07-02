package com.itda.moamoa.domain.chat.repository;

import com.itda.moamoa.domain.chat.entity.ChatRoom;
import com.itda.moamoa.domain.chat.entity.ChatRoomUser;
import com.itda.moamoa.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser,Long> {
    void deleteByUserAndRoom(User user, ChatRoom room);

    Optional<ChatRoomUser> findByUserIdAndRoomId(Long user, Long room);

    Optional <ChatRoomUser> findAllByRoomId(Long roomId);
}
