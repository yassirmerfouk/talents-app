package com.pulse.repository;

import com.pulse.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId);
    Page<Notification> findByReceiverIdOrderByCreatedAtDesc(Long receiverId, Pageable pageable);

    List<Notification> findByReceiverIdAndSeenFalse(Long receiverId);

}
