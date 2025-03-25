package com.notification_svc.controller.dto;

import com.notification_svc.model.NotificationType;
import com.notification_svc.model.SendStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificationResponse {

    private UUID id;
    private UUID userId;
    private String subject;
    private NotificationType type;
    private SendStatus status;
    private LocalDateTime sent;
}
