package com.quizmarkt.usermanagementservice.data.request;

import lombok.Data;

import java.util.List;

@Data
public class GoogleSubscriptionRequest {
    private boolean autoRenewingAndroid;
    private DataAndroid dataAndroid;
    private String developerPayloadAndroid;
    private boolean isAcknowledgedAndroid;
    private String obfuscatedAccountIdAndroid;
    private String obfuscatedProfileIdAndroid;
    private String packageNameAndroid;
    private String productId;
    private List<String> productIds;
    private int purchaseStateAndroid;
    private String purchaseToken;
    private String signatureAndroid;
    private long transactionDate;
    private String transactionId;
    private TransactionReceipt transactionReceipt;
}
