package com.notification_svc.model.mapper;

import com.notification_svc.controller.dto.NotificationPreferenceCreateRequest;
import com.notification_svc.controller.dto.NotificationPreferenceResponse;
import com.notification_svc.model.NotificationPreference;

import java.time.LocalDateTime;

public class NotificationMapper {

    public static NotificationPreference mapToNotificationPreference(NotificationPreferenceCreateRequest request) {

        return NotificationPreference.builder()
                .userId(request.getUserId())
                .enableNotification(request.isEnableNotification())
                .email(request.getEmail())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }


    public static NotificationPreferenceResponse mapToNotificationPreferenceResponce(NotificationPreference preference) {

        return NotificationPreferenceResponse.builder()
                .id(preference.getId())
                .userId(preference.getUserId())
                .enableNotification(preference.isEnableNotification())
                .email(preference.getEmail())
                .createdOn(preference.getCreatedOn())
                .updatedOn(preference.getUpdatedOn())
                .build();
    }
}
