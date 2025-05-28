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
    @Query("select c.room_id,c.room_name,c.lastMessageAt " +
            "(select count(*) from ChatMessage m where u.last_read_at > m.created_at) as unread " +
            "from ChatRoom c join ChatRoomUser u using(roomId)" +
            "where user_id = :userId" +
            "order by c.lastMessageAt desc")
    List<ChatRoomListDto> findChatRoomList(@Param("userId") Long userId);
}
