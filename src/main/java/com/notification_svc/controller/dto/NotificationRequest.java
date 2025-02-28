package com.notification_svc.controller.dto;

import com.notification_svc.model.NotificationType;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.UUID;

@Data
@Builder
public class NotificationRequest {

    @NonNull
    private UUID userId;
    @NonNull
    private String subject;
    @NonNull
    private String body;
    @NonNull
    private NotificationType type;
}
