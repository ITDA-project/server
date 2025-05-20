package com.itda.moamoa.domain.chat.entity;

import com.itda.moamoa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoomUser {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //유저의 채팅방에서의 권한(방장,참여자)
    @Enumerated(EnumType.STRING)
    private RoomRole role;

    private LocalDateTime lastReadAt; //마지막으로 채팅방을 읽은 시간, 안읽은 메시지 수 카운트 위함

    private LocalDateTime joinedAt;

    @PrePersist
    protected void onJoin() {
        this.joinedAt = LocalDateTime.now();
        this.lastReadAt = LocalDateTime.now();
    }

}
