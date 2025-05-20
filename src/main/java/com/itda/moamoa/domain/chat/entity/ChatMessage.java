package com.itda.moamoa.domain.chat.entity;

import com.itda.moamoa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Builder
public class ChatMessage {

    @Id @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    private String content;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
