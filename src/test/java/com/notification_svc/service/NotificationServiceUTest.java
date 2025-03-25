package com.notification_svc.service;

import com.notification_svc.controller.dto.NotificationPreferenceCreateRequest;
import com.notification_svc.exeption.ResourceAlreadyExistsException;
import com.notification_svc.exeption.ResourceNotFoundException;
import com.notification_svc.model.NotificationPreference;
import com.notification_svc.repository.NotificationPreferenceRepository;
import com.notification_svc.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class NotificationServiceUTest {
    @Mock
    private EmailService emailService;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private NotificationPreferenceRepository notificationPreferenceRepository;
    @InjectMocks
    private NotificationService notificationService;

    @Test
    void getNotificationPreferenceByUserId_happyPath() {

        UUID userId = UUID.randomUUID();
        NotificationPreference preference = NotificationPreference.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .enableNotification(true)
                .email("test@test.com")
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        when(notificationPreferenceRepository.findByUserId(userId)).thenReturn(Optional.of(preference));

        NotificationPreference result = notificationService.getNotificationPreferenceByUserId(userId);

        assertEquals(preference, result);

        verify(notificationPreferenceRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getNotificationPreferenceByUserId_thenThrowResourceAlreadyExistsException() {

        UUID userId = UUID.randomUUID();

        when(notificationPreferenceRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            notificationService.getNotificationPreferenceByUserId(userId);
        });

        verify(notificationPreferenceRepository, times(1)).findByUserId(userId);
    }

    @Test
    void createNotificationPreference_happyPath() {

        UUID userId = UUID.randomUUID();
        UUID preferenceId = UUID.randomUUID();

        NotificationPreferenceCreateRequest request = NotificationPreferenceCreateRequest.builder()
                .userId(userId)
                .email("test@test.com")
                .enableNotification(true)
                .build();

        when(notificationPreferenceRepository.findByUserId(userId)).thenReturn(Optional.empty());

        NotificationPreference notificationPreference = NotificationPreference.builder()
                .id(preferenceId)
                .userId(userId)
                .email("test@test.com")
                .enableNotification(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        when(notificationPreferenceRepository.save(any(NotificationPreference.class)))
                .thenReturn(notificationPreference);

        NotificationPreference result = notificationService.createNotificationPreference(request);

        assertEquals(notificationPreference.getEmail(), result.getEmail());
        assertEquals(notificationPreference.getUserId(), result.getUserId());
        assertEquals(notificationPreference.getId(), result.getId());
        verify(notificationPreferenceRepository, times(1)).save(any(NotificationPreference.class));
    }
    @Test
    void createNotificationPreference_thenThrowResourceAlreadyExistsException() {

        UUID userId = UUID.randomUUID();
        UUID preferenceId = UUID.randomUUID();

        NotificationPreferenceCreateRequest request = NotificationPreferenceCreateRequest.builder()
                .userId(userId)
                .email("test@test.com")
                .enableNotification(true)
                .build();

        NotificationPreference notificationPreference = NotificationPreference.builder()
                .id(preferenceId)
                .userId(userId)
                .email("test@test.com")
                .enableNotification(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
        when(notificationPreferenceRepository.findByUserId(userId)).thenReturn(Optional.of(notificationPreference));


        assertThrows(ResourceAlreadyExistsException.class, () -> {
            notificationService.createNotificationPreference(request);
        });
        verify(notificationPreferenceRepository, times(0)).save(any());
    }
}