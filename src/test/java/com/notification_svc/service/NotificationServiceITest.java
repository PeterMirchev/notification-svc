package com.notification_svc.service;

import com.notification_svc.controller.dto.NotificationPreferenceCreateRequest;
import com.notification_svc.controller.dto.NotificationPreferenceUpdateRequest;
import com.notification_svc.controller.dto.NotificationRequest;
import com.notification_svc.model.Notification;
import com.notification_svc.model.NotificationPreference;
import com.notification_svc.model.NotificationType;
import com.notification_svc.model.SendStatus;
import com.notification_svc.repository.NotificationPreferenceRepository;
import com.notification_svc.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class NotificationServiceITest {

    @Autowired
    private EmailService emailService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationPreferenceRepository notificationPreferenceRepository;
    @Autowired
    private NotificationService notificationService;

    @Test
    void updateNotificationPreference_happyPath() {

        UUID userId = UUID.randomUUID();
        NotificationPreferenceCreateRequest notificationPreferenceCreateRequest = NotificationPreferenceCreateRequest.builder()
                .userId(userId)
                .enableNotification(true)
                .email("test@test.com")
                .build();

        NotificationPreferenceUpdateRequest request = NotificationPreferenceUpdateRequest.builder()
                .userId(userId)
                .enableNotification(false)
                .email("test@test.com")
                .build();

        notificationService.createNotificationPreference(notificationPreferenceCreateRequest);


        notificationService.updateNotificationPreference(request);

        NotificationPreference preference = notificationService.getNotificationPreferenceByUserId(userId);

        assertEquals(preference.isEnableNotification(), request.isEnableNotification());
    }

    @Test
    void sendNotification_thenHappyPath() {

        UUID userId = UUID.randomUUID();
        NotificationPreferenceCreateRequest notificationPreferenceCreateRequest = NotificationPreferenceCreateRequest.builder()
                .userId(userId)
                .enableNotification(true)
                .email("test@test.com")
                .build();

        NotificationRequest request = NotificationRequest.builder()
                .userId(userId)
                .body("test")
                .subject("test")
                .email("test@test.com")
                .type(NotificationType.NOTIFICATION)
                .build();

        notificationService.createNotificationPreference(notificationPreferenceCreateRequest);

        NotificationPreference preference = notificationService.getNotificationPreferenceByUserId(userId);
        Notification notification = Notification.builder()
                .userId(preference.getUserId())
                .subject(request.getSubject())
                .body(request.getBody())
                .type(request.getType())
                .status(SendStatus.SUCCESS)
                .sent(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
        emailService.sendEmail(preference.getEmail(), request.getSubject(), request.getBody());

        notificationService.sendNotification(request);

        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);

        assertEquals(notifications.get(0).getUserId(), userId);
    }

    @Test
    void switchNotificationPreference_happyPath() {

        UUID userId = UUID.randomUUID();

        NotificationPreferenceCreateRequest notificationPreferenceCreateRequest = NotificationPreferenceCreateRequest.builder()
                .userId(userId)
                .enableNotification(true)
                .email("test@test.com")
                .build();

        notificationService.createNotificationPreference(notificationPreferenceCreateRequest);

        NotificationPreference updatedPreference = notificationService.switchNotificationPreference(userId);

        assertNotNull(updatedPreference);
        assertFalse(updatedPreference.isEnableNotification());

        NotificationPreference preferenceInDb = notificationPreferenceRepository.findByUserId(userId).orElse(null);
        assertNotNull(preferenceInDb);
        assertFalse(preferenceInDb.isEnableNotification());
    }
}
