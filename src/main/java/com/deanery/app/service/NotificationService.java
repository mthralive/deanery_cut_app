package com.deanery.app.service;

import com.deanery.app.error.exception.UserNotFoundException;
import com.deanery.app.model.Notification;
import com.deanery.app.model.User;
import com.deanery.app.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public Notification findByUUID(UUID id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Long hasUnreadNotifications(User user) {
        return notificationRepository.countByUserAndIsCheckedFalse(user);
    }

    @Transactional
    public void checkedNotificationsWithoutPage(User user) {
        List<Notification> notifications = notificationRepository.findByUser(user);
        for (Notification notification : notifications) {
            notification.setIsChecked(true);
            notificationRepository.save(notification);
        }
    }

    @Transactional(readOnly = true)
    public Page<Notification> findByUserOrderByIsChecked(User user, Pageable pageable) {
        return notificationRepository
                .findByUserOrderByIsChecked(user, pageable);
    }

    @Transactional
    public Boolean delete(User user,List<UUID> notifs){
        for(UUID ni : notifs){
            Notification n = findByUUID(ni);
            notificationRepository.delete(n);
        }
        return hasUnreadNotifications(user) > 0L;
    }

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }
}
