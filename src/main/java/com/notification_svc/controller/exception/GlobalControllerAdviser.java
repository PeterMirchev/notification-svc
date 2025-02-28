package com.notification_svc.controller.exception;

import com.notification_svc.exeption.NotificationDisabledException;
import com.notification_svc.exeption.NotificationServiceException;
import com.notification_svc.exeption.ResourceAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdviser {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<String> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception) {

        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotificationServiceException.class)
    public ResponseEntity<String> handleNotificationServiceException(NotificationServiceException exception) {

        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }@ExceptionHandler(NotificationDisabledException.class)
    public ResponseEntity<String> handleNotificationDisabledException(NotificationDisabledException exception) {

        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }
}
