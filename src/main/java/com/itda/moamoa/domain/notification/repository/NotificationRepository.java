package com.itda.moamoa.domain.notification.repository;

import com.itda.moamoa.domain.notification.entity.Notification;
import com.itda.moamoa.domain.user.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserAndIdLessThanOrderByIdDesc(User user, Long cursor, PageRequest pageRequest);
    Optional<Notification> findByIdAndUser(Long notificationId, User user);
    List<Notification> findByUserAndIsReadFalse(User user);
}
