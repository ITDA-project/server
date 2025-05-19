package com.itda.moamoa.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoom {

    @Id
    @GeneratedValue
    @Column(name="room_id")
    private Long id; //채팅방 ID

    //채팅방 제목?
    private String roomName;

    //만들어진 시간
    private LocalDateTime createdAt = LocalDateTime.now();

    private Boolean isDeleted = false;

    public void softDelete(){
        this.isDeleted = true;
    }

    public Boolean isDeleted(){
        return this.isDeleted;
    }

}
