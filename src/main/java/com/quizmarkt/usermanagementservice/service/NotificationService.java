package com.quizmarkt.usermanagementservice.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.quizmarkt.usermanagementservice.data.entity.AppConfig;
import com.quizmarkt.usermanagementservice.data.entity.DeviceInfo;
import com.quizmarkt.usermanagementservice.data.entity.User;
import com.quizmarkt.usermanagementservice.data.repository.UserRepository;
import com.quizmarkt.usermanagementservice.data.request.MulticastNotificationRequest;
import com.quizmarkt.usermanagementservice.manager.AppConfigManager;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author anercan
 */
@Service
@AllArgsConstructor
@Slf4j
@DependsOn("appConfigManager")
public class NotificationService {

    private final AppConfigManager appConfigManager;
    private final UserRepository userRepository;

    @PostConstruct
    public void initializeFirebase() {
        appConfigManager.getInMemoryAppList().forEach(app -> {
                    try {
                        InputStream serviceAccountStream = app.getServiceConfigFile();
                        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);

                        FirebaseOptions options = new FirebaseOptions.Builder()
                                .setCredentials(credentials)
                                .build();

                        if (FirebaseApp.getApps().isEmpty()) {
                            FirebaseApp firebaseApp = FirebaseApp.initializeApp(options, app.getPackageName());
                            log.info("FirebaseApp initialized for {}", firebaseApp.getName());
                        }
                    } catch (Exception e) {
                        log.error("initializeFirebase got exception", e);
                    }
                }
        );
    }

    public void sendNotification(MulticastNotificationRequest request) {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                log.warn("Firebase app couldn't found.Notification won't be send.");
                return;
            }
            Notification notification = Notification.builder().setTitle(request.getTitle()).setBody(request.getBody()).build();
            MulticastMessage message = MulticastMessage.builder()
                    .setNotification(notification)
                    .addAllTokens(getDeviceTokenList(request.getUserIDList()))
                    .build();

            BatchResponse response = FirebaseMessaging.getInstance(getInstance(request.getAppId()))
                    .sendEachForMulticast(message);

            log.info("Notification sent: {}",
                    response.getResponses().stream()
                            .map(SendResponse::getMessageId)
                            .collect(Collectors.joining(",")));
        } catch (Exception e) {
            log.error("sendNotification got exception.", e);
        }
    }

    private FirebaseApp getInstance(Integer appId) {
        String appPackageName = appConfigManager.getInMemoryAppList().stream().filter(app -> app.getAppId().equals(appId)).findFirst().map(AppConfig::getPackageName).orElseThrow();
        return FirebaseApp.getInstance(appPackageName);
    }

    private List<String> getDeviceTokenList(List<String> userIDList) {
        Iterable<User> allById = userRepository.findAllById(userIDList);
        return StreamSupport.stream(allById.spliterator(), false)
                .filter(user -> Objects.nonNull(user.getDeviceInfo()) && StringUtils.isNotEmpty(user.getDeviceInfo().getFcmToken()))
                .map(User::getDeviceInfo)
                .map(DeviceInfo::getFcmToken)
                .toList();
    }

}
