package com.pulse.controller;

import com.pulse.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserNotifications(){
        return new ResponseEntity<>(
                notificationService.getUserNotifications(),
                HttpStatus.OK
        );
    }

    @GetMapping("/page")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserNotifications(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        return new ResponseEntity<>(
                notificationService.getUserNotifications(page, size),
                HttpStatus.OK
        );
    }

    @PostMapping("/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> readUserNotifications(){
        notificationService.readUserNotifications();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/click")
    public ResponseEntity<?> clickOnNotification(
            @PathVariable Long id
    ){
        notificationService.clickOnNotification(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteNotification(
            @PathVariable Long id
    ){
        notificationService.deleteNotification(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/send-notification/specific")
    public void sendNotificationToSpecificUser(){
        notificationService.sendNotificationToAllUsers();
    }
}
