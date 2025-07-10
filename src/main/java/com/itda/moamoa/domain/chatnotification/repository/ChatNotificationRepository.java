package com.itda.moamoa.domain.chatnotification.repository;

import com.itda.moamoa.domain.chatnotification.entity.ChatNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatNotificationRepository extends JpaRepository<ChatNotification, Long> {
    Optional<ChatNotification> findChatNotificationById(Long id);
}
