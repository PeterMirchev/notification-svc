package com.notification_svc.repository;

import com.notification_svc.model.Notification;
import com.notification_svc.model.NotificationPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

}
