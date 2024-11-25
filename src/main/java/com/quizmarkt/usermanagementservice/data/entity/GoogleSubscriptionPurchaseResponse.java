package com.quizmarkt.usermanagementservice.data.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.Data;

/**
 * @author anercan
 */

@Data
@DynamoDBDocument
public class GoogleSubscriptionPurchaseResponse {

    @DynamoDBAttribute
    private Integer acknowledgementState;
    @DynamoDBAttribute
    private Boolean autoRenewing;
    @DynamoDBAttribute
    private Long autoResumeTimeMillis;
    @DynamoDBAttribute
    private Integer cancelReason;
    @DynamoDBAttribute
    private String countryCode;
    @DynamoDBAttribute
    private String developerPayload;
    @DynamoDBAttribute
    private String emailAddress;
    @DynamoDBAttribute
    private Long expiryTimeMillis;
    @DynamoDBAttribute
    private String externalAccountId;
    @DynamoDBAttribute
    private String familyName;
    @DynamoDBAttribute
    private String givenName;
    @DynamoDBAttribute
    private String kind;
    @DynamoDBAttribute
    private String linkedPurchaseToken;
    @DynamoDBAttribute
    private String obfuscatedExternalAccountId;
    @DynamoDBAttribute
    private String obfuscatedExternalProfileId;
    @DynamoDBAttribute
    private String orderId;
    @DynamoDBAttribute
    private Integer paymentState;
    @DynamoDBAttribute
    private Long priceAmountMicros;
    @DynamoDBAttribute
    private String priceCurrencyCode;
    @DynamoDBAttribute
    private String profileId;
    @DynamoDBAttribute
    private String profileName;
    @DynamoDBAttribute
    private String promotionCode;
    @DynamoDBAttribute
    private Integer promotionType;
    @DynamoDBAttribute
    private Integer purchaseType;
    @DynamoDBAttribute
    private Long startTimeMillis;
    @DynamoDBAttribute
    private Long userCancellationTimeMillis;
}
