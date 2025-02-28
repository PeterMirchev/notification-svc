package com.notification_svc.exeption;

public class NotificationServiceException extends RuntimeException {
    public NotificationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}