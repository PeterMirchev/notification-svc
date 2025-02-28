package com.notification_svc.service;

import com.notification_svc.controller.dto.NotificationPreferenceCreateRequest;
import com.notification_svc.controller.dto.NotificationPreferenceUpdateRequest;
import com.notification_svc.exeption.ResourceAlreadyExistsException;
import com.notification_svc.model.NotificationPreference;
import com.notification_svc.model.mapper.NotificationMapper;
import com.notification_svc.repository.NotificationPreferenceRepository;
import com.notification_svc.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationService {
    private final EmailService emailService;
    private final NotificationRepository notificationRepository;
    private final NotificationPreferenceRepository notificationPreferenceRepository;
    public NotificationService(EmailService emailService, NotificationRepository notificationRepository,
                               NotificationPreferenceRepository notificationPreferenceRepository) {
        this.emailService = emailService;
        this.notificationRepository = notificationRepository;
        this.notificationPreferenceRepository = notificationPreferenceRepository;
    }


    public NotificationPreference getNotificationPreferenceByUserId(UUID userId) {

        return notificationPreferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceAlreadyExistsException("Notification preferences already exist for this user id - %s.".formatted(userId)));
    }
    public NotificationPreference createNotificationPreference(NotificationPreferenceCreateRequest request) {

        Optional<NotificationPreference> preference = Optional.ofNullable(getNotificationPreferenceByUserId(request.getUserId()));

        if (preference.isPresent()) {
            throw new ResourceAlreadyExistsException("Notification preferences already exist for this user id - %s.".formatted(request.getUserId()));
        }

        NotificationPreference newPreference = NotificationMapper.mapToNotificationPreference(request);

        return notificationPreferenceRepository.save(newPreference);
    }

    public NotificationPreference updateNotificationPreference(NotificationPreferenceUpdateRequest request) {

        NotificationPreference notificationPreference = getNotificationPreferenceByUserId(request.getUserId());

        notificationPreference.setEnableNotification(request.isEnableNotification());
        notificationPreference.setEmail(request.getEmail());
        notificationPreference.setUpdatedOn(LocalDateTime.now());

        return notificationPreferenceRepository.save(notificationPreference);
    }
}
