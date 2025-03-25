package com.notification_svc.controller;

import com.notification_svc.controller.dto.*;
import com.notification_svc.model.Notification;
import com.notification_svc.model.NotificationPreference;
import com.notification_svc.model.mapper.NotificationMapper;
import com.notification_svc.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/preference")
    public ResponseEntity<NotificationPreferenceResponse> createNotificationPreference(@RequestBody NotificationPreferenceCreateRequest request) {

        NotificationPreference preference = notificationService.createNotificationPreference(request);

        NotificationPreferenceResponse response = NotificationMapper.mapToNotificationPreferenceResponse(preference);

        return  ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/preference")
    public ResponseEntity<NotificationPreferenceResponse> updateNotificationPreference(@RequestBody NotificationPreferenceUpdateRequest request) {

        NotificationPreference preference = notificationService.updateNotificationPreference(request);

        NotificationPreferenceResponse response = NotificationMapper.mapToNotificationPreferenceResponse(preference);

        return  ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/toggle")
    public ResponseEntity<NotificationPreferenceResponse> switchNotification(@RequestParam(name = "userId") UUID userId) {

        NotificationPreference preference = notificationService.switchNotificationPreference(userId);

        NotificationPreferenceResponse response = NotificationMapper.mapToNotificationPreferenceResponse(preference);

        return  ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/notification")
    public ResponseEntity<NotificationResponse> sendNotification(@RequestBody NotificationRequest notificationRequest) {

        Notification notification = notificationService.sendNotification(notificationRequest);

        NotificationResponse response = NotificationMapper.mapToNotificationResponse(notification);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/preference")
    public ResponseEntity<NotificationPreferenceResponse> getNotificationPreference(@RequestParam(name = "userId") UUID userId) {

        NotificationPreference preference = notificationService.getNotificationPreferenceByUserId(userId);

        NotificationPreferenceResponse response = NotificationMapper.mapToNotificationPreferenceResponse(preference);

        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<NotificationResponse>> getAllNotifications(@RequestParam(name = "userId") UUID userId) {

        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);

        List<NotificationResponse> responses = notifications
                .stream()
                .map(NotificationMapper::mapToNotificationResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }
}
