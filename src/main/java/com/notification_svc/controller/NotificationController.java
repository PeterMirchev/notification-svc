package com.notification_svc.controller;

import com.notification_svc.controller.dto.NotificationPreferenceCreateRequest;
import com.notification_svc.controller.dto.NotificationPreferenceResponse;
import com.notification_svc.controller.dto.NotificationPreferenceUpdateRequest;
import com.notification_svc.model.NotificationPreference;
import com.notification_svc.model.mapper.NotificationMapper;
import com.notification_svc.service.EmailService;
import com.notification_svc.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final EmailService emailService;

    public NotificationController(NotificationService notificationService, EmailService emailService) {
        this.notificationService = notificationService;
        this.emailService = emailService;
    }

    @PostMapping("/preference")
    public ResponseEntity<NotificationPreferenceResponse> createNotificationPreference(@RequestBody NotificationPreferenceCreateRequest request) {

        NotificationPreference preference = notificationService.createNotificationPreference(request);

        NotificationPreferenceResponse response = NotificationMapper.mapToNotificationPreferenceResponce(preference);

        return  ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/preference")
    public ResponseEntity<NotificationPreferenceResponse> updateNotificationPreference(@RequestBody NotificationPreferenceUpdateRequest request) {

        NotificationPreference preference = notificationService.updateNotificationPreference(request);

        NotificationPreferenceResponse response = NotificationMapper.mapToNotificationPreferenceResponce(preference);

        return  ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
