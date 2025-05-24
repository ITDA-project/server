package com.itda.moamoa.domain.chatnotification.repository;

import com.itda.moamoa.domain.chatnotification.entity.ChatNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//@Repository 생략
public interface ChatNotificationRepository extends JpaRepository<ChatNotification, Long> { //대상 엔티티, 기본키
    //id로 채팅 알림 찾기
    //Optional - 빈 경우 NPE x
    Optional<ChatNotification> findChatNotificationById(Long id);
}
