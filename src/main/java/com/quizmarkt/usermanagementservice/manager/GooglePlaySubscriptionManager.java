package com.quizmarkt.usermanagementservice.manager;

import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.model.SubscriptionPurchase;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@Slf4j
public class GooglePlaySubscriptionManager {

    private static final String PACKAGE_NAME_LIFEINTHEUK = "com.quizmarkt.lifeintheuk";

    private AndroidPublisher initPublisher() {
        InputStream serviceAccountStream = getServiceConfigFile();
        try (serviceAccountStream) {
            if (serviceAccountStream == null) {
                log.error("googleAuth json couldn't found for {}", PACKAGE_NAME_LIFEINTHEUK);
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

    private InputStream getServiceConfigFile() {
        try (InputStream inputStream = Files.newInputStream(Paths.get("/app/config/googleAuth-" + PACKAGE_NAME_LIFEINTHEUK + ".json"))) {
            return inputStream;
        } catch (Exception e) {
            log.error("Failed to read googleAuth.json for " + PACKAGE_NAME_LIFEINTHEUK, e);
            return null;
        }
    }

    public SubscriptionPurchase getSubscriptionData(String subscriptionId, String purchaseToken, String userId) {
        try {
            AndroidPublisher publisher = initPublisher();
            if (publisher == null) {
                return null;
            }

            AndroidPublisher.Purchases.Subscriptions.Get request = publisher
                    .purchases()
                    .subscriptions()
                    .get(PACKAGE_NAME_LIFEINTHEUK, subscriptionId, purchaseToken);

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
