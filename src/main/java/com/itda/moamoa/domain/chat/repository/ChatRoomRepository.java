package com.itda.moamoa.domain.chat.repository;

import com.itda.moamoa.domain.chat.dto.ChatRoomListDto;
import com.itda.moamoa.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    @Query("select new com.itda.moamoa.domain.chat.dto.ChatRoomListDto(" +
            "c.id," +
            "c.roomName," +
            "c.lastMessage, " +
            "c.lastMessageAt, " +
            "(" +
            "select count(m) from ChatMessage m where m.room.id = c.id and u.lastReadAt < m.createdAt" +
            ")" +
            ") " +
            "from ChatRoom c join ChatRoomUser u on c.id = u.room.id " +
            "where u.user.id = :userId " +
            "order by c.lastMessageAt desc")
    List<ChatRoomListDto> findChatRoomList(@Param("userId") Long userId);
}
