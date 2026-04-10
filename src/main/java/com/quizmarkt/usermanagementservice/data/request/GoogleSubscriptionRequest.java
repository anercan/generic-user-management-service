package com.quizmarkt.usermanagementservice.data.request;

import lombok.Data;

@Data
public class GoogleSubscriptionRequest {
    private String id;
    private String productId;
    private long transactionDate;
    private String purchaseToken;
    private String platform;
    private String store;
    private int quantity;
    private String purchaseState;
    private boolean isAutoRenewing;
    private String transactionId;
    private boolean autoRenewingAndroid;
    private String dataAndroid;
    private String signatureAndroid;
    private boolean isAcknowledgedAndroid;
    private String packageNameAndroid;
    private String obfuscatedAccountIdAndroid;
    private String obfuscatedProfileIdAndroid;
    private String developerPayloadAndroid;
    private boolean isSuspendedAndroid;

    public int getPurchaseStateAndroid() {
        return switch (purchaseState) {
            case "purchased" -> 0;
            case "canceled" -> 1;
            case "pending" -> 2;
            default -> -1;
        };
    }
}
