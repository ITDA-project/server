package com.itda.moamoa.domain.chat.repository;

import com.itda.moamoa.domain.chat.entity.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser,Long> {
    void deleteByUserIdAndRoomId(Long userId,Long roomId);
}
