package com.notification_svc.service;

public interface EmailService {

    void sendEmail(String to, String subject, String body);
}
