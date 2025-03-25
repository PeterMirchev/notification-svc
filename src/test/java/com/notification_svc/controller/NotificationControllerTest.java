package com.notification_svc.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification_svc.controller.dto.NotificationPreferenceCreateRequest;
import com.notification_svc.controller.dto.NotificationPreferenceUpdateRequest;
import com.notification_svc.controller.dto.NotificationRequest;
import com.notification_svc.model.Notification;
import com.notification_svc.model.NotificationPreference;
import com.notification_svc.model.NotificationType;
import com.notification_svc.model.SendStatus;
import com.notification_svc.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.notification_svc.TestBuilder.aRandomNotificationPreference;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @MockitoBean
    private NotificationService notificationService;
    @Autowired
    private MockMvc mockMvc;
    @Test
    void createNotificationPreference_happyPath() throws Exception {

        UUID userId = UUID.randomUUID();
        NotificationPreferenceCreateRequest requestNotification = NotificationPreferenceCreateRequest.builder()
                .userId(userId)
                .email("test@test.com")
                .enableNotification(true)
                .build();
        when(notificationService.createNotificationPreference(any())).thenReturn(aRandomNotificationPreference());

        MockHttpServletRequestBuilder request = post("/api/v1/notifications/preference")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(requestNotification));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("email").value("test@test.com"))
                .andExpect(jsonPath("enableNotification").value("true"));

    }

    @Test
    void updateNotificationPreference_happyPath() throws Exception {

        UUID userId = UUID.randomUUID();
        NotificationPreferenceUpdateRequest requestUpdate = NotificationPreferenceUpdateRequest.builder()
                .userId(userId)
                .email("test@test.com")
                .enableNotification(false)
                .build();

        when(notificationService.updateNotificationPreference(any())).thenReturn(aRandomNotificationPreference());

        MockHttpServletRequestBuilder request = put("/api/v1/notifications/preference")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(requestUpdate));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("test@test.com"))
                .andExpect(jsonPath("enableNotification").value(true));
    }
    @Test
    void switchNotification_happyPath() throws Exception {

        UUID userId = UUID.randomUUID();

        when(notificationService.switchNotificationPreference(any())).thenReturn(aRandomNotificationPreference());

        MockHttpServletRequestBuilder request = put("/api/v1/notifications/toggle")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", String.valueOf(userId));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").value("test@test.com"))
                .andExpect(jsonPath("enableNotification").value(true));
    }

    @Test
    void sendNotification_happyPath() throws Exception {

        UUID userId = UUID.randomUUID();
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(userId)
                .subject("Test Subject")
                .body("Test Body")
                .email("test@test.com")
                .type(NotificationType.NOTIFICATION)
                .build();

        Notification mockNotification = new Notification();
        mockNotification.setUserId(userId);
        mockNotification.setSubject(notificationRequest.getSubject());
        mockNotification.setBody(notificationRequest.getBody());
        mockNotification.setType(notificationRequest.getType());
        mockNotification.setStatus(SendStatus.SUCCESS);
        mockNotification.setSent(LocalDateTime.now());

        when(notificationService.sendNotification(any())).thenReturn(mockNotification);

        MockHttpServletRequestBuilder request = post("/api/v1/notifications/notification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(notificationRequest));

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("userId").value(userId.toString()))
                .andExpect(jsonPath("subject").value(notificationRequest.getSubject()))
                .andExpect(jsonPath("status").value(SendStatus.SUCCESS.toString()));
    }

    @Test
    void getNotificationPreference_happyPath() throws Exception {

        UUID preferenceId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        NotificationPreference preference = NotificationPreference.builder()
                .id(preferenceId)
                .email("test@test.com")
                .userId(userId)
                .enableNotification(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        when(notificationService.getNotificationPreferenceByUserId(userId)).thenReturn(preference);

        MockHttpServletRequestBuilder request = get("/api/v1/notifications/preference")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userId", String.valueOf(userId));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId").value(userId.toString()))
                .andExpect(jsonPath("email").value(preference.getEmail()));
    }
}