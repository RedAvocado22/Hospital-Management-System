package com.hospital.hms.notification.repository;

import com.hospital.hms.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByRecipient_IdOrderByCreatedAtDesc(UUID accountId);

    List<Notification> findByRecipient_IdAndIsReadFalse(UUID accountId);
}
