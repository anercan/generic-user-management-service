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

    private AndroidPublisher initPublisher(int appId) {
        AppConfig app = appConfigManager.getInMemoryAppWithId(appId);
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
            AndroidPublisher publisher = initPublisher(appId);
            if (publisher == null) {
                log.error("AndroidPublisher could not set appId:{} userId:{}", appId, userId);
                return null;
            }

            AndroidPublisher.Purchases.Subscriptions.Get request = publisher
                    .purchases()
                    .subscriptions()
                    .get(app.getPackageName(), subscriptionId, purchaseToken);

            SubscriptionPurchase execute = request.execute();
            log.info("getSubscriptionData called for userId:{} response:{}", userId, subscriptionPurchaseToString(execute));
            return execute;
        } catch (Exception e) {
            log.error("verifySubscription got exception userId:{}", userId, e);
            return null;
        }
    }

    private String subscriptionPurchaseToString(SubscriptionPurchase sp) {
        if (sp == null) {
            return "SubscriptionPurchase is null";
        }
        try {
            return "SubscriptionPurchase{" +
                    "acknowledgementState=" + sp.getAcknowledgementState() +
                    ", autoRenewing=" + sp.getAutoRenewing() +
                    ", autoResumeTimeMillis=" + sp.getAutoResumeTimeMillis() +
                    ", cancelReason=" + sp.getCancelReason() +
                    ", cancelSurveyResult=" + sp.getCancelSurveyResult() +
                    ", countryCode='" + sp.getCountryCode() + '\'' +
                    ", developerPayload='" + sp.getDeveloperPayload() + '\'' +
                    ", emailAddress='" + sp.getEmailAddress() + '\'' +
                    ", expiryTimeMillis=" + sp.getExpiryTimeMillis() +
                    ", externalAccountId='" + sp.getExternalAccountId() + '\'' +
                    ", familyName='" + sp.getFamilyName() + '\'' +
                    ", givenName='" + sp.getGivenName() + '\'' +
                    ", introductoryPriceInfo=" + sp.getIntroductoryPriceInfo() +
                    ", kind='" + sp.getKind() + '\'' +
                    ", linkedPurchaseToken='" + sp.getLinkedPurchaseToken() + '\'' +
                    ", obfuscatedExternalAccountId='" + sp.getObfuscatedExternalAccountId() + '\'' +
                    ", obfuscatedExternalProfileId='" + sp.getObfuscatedExternalProfileId() + '\'' +
                    ", orderId='" + sp.getOrderId() + '\'' +
                    ", paymentState=" + sp.getPaymentState() +
                    ", priceAmountMicros=" + sp.getPriceAmountMicros() +
                    ", priceChange=" + sp.getPriceChange() +
                    ", priceCurrencyCode='" + sp.getPriceCurrencyCode() + '\'' +
                    ", profileId='" + sp.getProfileId() + '\'' +
                    ", profileName='" + sp.getProfileName() + '\'' +
                    ", promotionCode='" + sp.getPromotionCode() + '\'' +
                    ", promotionType=" + sp.getPromotionType() +
                    ", purchaseType=" + sp.getPurchaseType() +
                    ", startTimeMillis=" + sp.getStartTimeMillis() +
                    ", userCancellationTimeMillis=" + sp.getUserCancellationTimeMillis() +
                    '}';
        } catch (Exception e) {
            log.error("subscriptionPurchaseToString got exception", e);
            return "subscriptionPurchaseToString got exception";
        }
    }

    public boolean isSubscriptionValid(SubscriptionPurchase subscription) {
        return subscription != null && subscription.getExpiryTimeMillis() > System.currentTimeMillis();
    }
}
