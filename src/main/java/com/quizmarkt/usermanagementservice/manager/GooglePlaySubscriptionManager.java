package com.quizmarkt.usermanagementservice.manager;

import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.model.SubscriptionPurchase;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.quizmarkt.usermanagementservice.data.entity.AppConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@Slf4j
@AllArgsConstructor
public class GooglePlaySubscriptionManager {

    private final AppConfigManager appConfigManager;

    private AndroidPublisher initPublisher() {
        AppConfig app = appConfigManager.getInMemoryAppWithId(1);
        InputStream serviceAccountStream = app.getServiceConfigFile();
        try (serviceAccountStream) {
            if (serviceAccountStream == null) {
                log.error("googleAuth json couldn't found for {}", app.getPackageName());
                return null;
            }
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(serviceAccountStream)
                    .createScoped(AndroidPublisherScopes.ANDROIDPUBLISHER);

            return new AndroidPublisher.Builder(
                    new com.google.api.client.http.javanet.NetHttpTransport(),
                    new GsonFactory(),
                    new HttpCredentialsAdapter(credentials)
            )
                    .setApplicationName("Life In the UK Test 2025")
                    .build();
        } catch (Exception e) {
            log.error("initPublisher got exeption", e);
            return null;
        }
    }

    public SubscriptionPurchase getSubscriptionData(String subscriptionId, String purchaseToken, String userId, int appId) {
        AppConfig app = appConfigManager.getInMemoryAppWithId(appId);
        try {
            AndroidPublisher publisher = initPublisher();
            if (publisher == null) {
                return null;
            }

            AndroidPublisher.Purchases.Subscriptions.Get request = publisher
                    .purchases()
                    .subscriptions()
                    .get(app.getPackageName(), subscriptionId, purchaseToken);

            return request.execute();
        } catch (Exception e) {
            log.error("verifySubscription got exception userId:{}", userId, e);
            return null;
        }

    }

    public boolean isSubscriptionValid(SubscriptionPurchase subscription) {
        return subscription != null
                && subscription.getExpiryTimeMillis() > System.currentTimeMillis()
                && subscription.getPaymentState() == 1;
    }
}
