package com.quizmarkt.usermanagementservice.controller;

import com.quizmarkt.usermanagementservice.data.request.MulticastNotificationRequest;
import com.quizmarkt.usermanagementservice.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author anercan
 */

@RestController
@AllArgsConstructor
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send-multicast")
    public ResponseEntity<Void> send(@RequestBody MulticastNotificationRequest request) {
        notificationService.sendNotification(request);
        return ResponseEntity.ok().build();
    }
}
