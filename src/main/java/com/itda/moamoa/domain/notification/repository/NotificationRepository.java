package com.itda.moamoa.domain.notification.repository;

import com.itda.moamoa.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    //사용자 id 기준 전체 알림 조회 - 시간순 정렬 조회(최신순)
    List<Notification> findAllByReceiverIdOrderByCreatedAtDesc(Long receiverId);

    //전체 알림 중 개별 알림 조회
    Optional<Notification> findByIdAndReceiverId(Long notificationId, Long receiverId);
    //Optional은 null일 수 있는 값을 감싸는 wrapper 객체 - 값이 없어도 NPE x

}
