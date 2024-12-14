package com.deanery.app.repository;

import com.deanery.app.model.Notification;
import com.deanery.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Page<Notification> findByUserOrderByIsChecked(User user, Pageable pageable);

    List<Notification> findByUser(User user);

    Long countByUserAndIsCheckedFalse(User user);
}
