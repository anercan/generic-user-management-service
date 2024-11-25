package com.quizmarkt.usermanagementservice.data.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Data;

import java.util.List;

/**
 * @author anercan
 */

@Data
@DynamoDBDocument
public class GoogleSubscriptionRequest {
    @DynamoDBAttribute
    private boolean autoRenewingAndroid;
    @DynamoDBAttribute
    private String developerPayloadAndroid;
    @DynamoDBAttribute
    private boolean isAcknowledgedAndroid;
    @DynamoDBAttribute
    private String obfuscatedAccountIdAndroid;
    @DynamoDBAttribute
    private String obfuscatedProfileIdAndroid;
    @DynamoDBAttribute
    private String packageNameAndroid;
    @DynamoDBAttribute
    private String productId;
    @DynamoDBAttribute
    private List<String> productIds;
    @DynamoDBAttribute
    private int purchaseStateAndroid;
    @DynamoDBAttribute
    private String purchaseToken;
    @DynamoDBAttribute
    private String signatureAndroid;
    @DynamoDBAttribute
    private long transactionDate;
    @DynamoDBAttribute
    private String transactionId;
    @DynamoDBAttribute
    private GoogleTransactionReceipt transactionReceipt;


}
