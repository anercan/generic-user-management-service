package com.quizmarkt.usermanagementservice.data.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Data;

@Data
@DynamoDBDocument
public class GoogleTransactionReceipt {
    @DynamoDBAttribute
    private String orderId;
    @DynamoDBAttribute
    private String packageName;
    @DynamoDBAttribute
    private String productId;
    @DynamoDBAttribute
    private long purchaseTime;
    @DynamoDBAttribute
    private int purchaseState;
    @DynamoDBAttribute
    private String purchaseToken;
    @DynamoDBAttribute
    private int quantity;
    @DynamoDBAttribute
    private boolean autoRenewing;
    @DynamoDBAttribute
    private boolean acknowledged;
}