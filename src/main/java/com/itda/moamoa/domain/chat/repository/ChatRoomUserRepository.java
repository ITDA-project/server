package com.itda.moamoa.domain.chat.repository;

import com.itda.moamoa.domain.chat.dto.ChatRoomParticipantsDto;
import com.itda.moamoa.domain.chat.entity.ChatRoom;
import com.itda.moamoa.domain.chat.entity.ChatRoomUser;
import com.itda.moamoa.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser,Long> {
    void deleteByUserAndRoom(User user, ChatRoom room);

    Optional<ChatRoomUser> findByUserIdAndRoomId(Long user, Long room);

    @Query("select new com.itda.moamoa.domain.chat.dto.ChatRoomParticipantsDto(" +
            "u.name," +
            "u.image) " +
            "from ChatRoomUser c join c.user u " +
            "where c.room.id = :roomId")
    List<ChatRoomParticipantsDto> findByRoomId(@Param("roomId") Long roomId);

    @Query("select count(c) > 0 " +
            "from ChatRoomUser c " +
            "join c.user u " +
            "where u.username = :username and c.room.id = :roomId")
    boolean existsByUsernameAndRoomId(@Param("username") String username,
                                    @Param("roomId") Long roomId);

}
