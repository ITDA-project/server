package com.itda.moamoa.domain.chat.repository;

import com.itda.moamoa.domain.chat.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage,Long> {
    @Query("select m " +
            "from ChatMessage m join fetch m.sender " +
            "where m.room.id = :roomId and m.id < :cursor " +
            "order by m.id desc")
    List<ChatMessage> findByRoomIdAndIdLessThanOrderByIdDesc(Long roomId, Long cursor, Pageable pageable);
}
