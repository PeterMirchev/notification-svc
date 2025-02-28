package com.notification_svc.exeption;

public class NotificationDisabledException extends RuntimeException {
    public NotificationDisabledException(String message) {
        super(message);
    }
}