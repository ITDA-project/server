package com.itda.moamoa.domain.chatnotification.entity;

import com.itda.moamoa.domain.chat.entity.ChatMessage;
import com.itda.moamoa.domain.chat.entity.ChatRoom;
import com.itda.moamoa.domain.chatnotification.dto.ChatNotificationType;
import com.itda.moamoa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "chat_notification")
public class ChatNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_notification_id")
    private Long id;

    //채팅 메시지를 보내는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    //채팅 메시지 받는 사람
    @ManyToOne(fetch = FetchType.LAZY) //특정 유저는 여러 알림을 받음
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    //채팅 메시지 발생 채팅방
    //@ManyToOne(fetch = FetchType.LAZY)
    //private ChatRoom chatRoom;

    private String chatRoomName;

    private Long chatMessageId;

    //해당 채팅 메시지
    //@ManyToOne(fetch = FetchType.LAZY) //메시지 하나에서 여러 알림
    //private ChatMessage chatMessage;

    //알림 유형
    @Enumerated(EnumType.STRING)
    private ChatNotificationType chatNotificationType; //CHAT, INVITE

    //내용
    private String content;

    //알림 생성 시간
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    //알림 읽음 여부
    @Builder.Default
    @Column(name = "is_read")
    private boolean read = false;

}
