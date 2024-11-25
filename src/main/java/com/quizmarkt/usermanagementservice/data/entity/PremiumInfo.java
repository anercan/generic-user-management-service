package com.quizmarkt.usermanagementservice.data.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.quizmarkt.usermanagementservice.data.enums.PremiumType;
import com.quizmarkt.usermanagementservice.data.enums.StoreType;
import lombok.Data;

/**
 * @author anercan
 */

@Data
@DynamoDBDocument
public class PremiumInfo {

    @DynamoDBAttribute(attributeName = "premiumType")
    @DynamoDBTypeConvertedEnum
    private PremiumType premiumType = PremiumType.NONE;

    @DynamoDBAttribute(attributeName = "expireDate")
    private Long expireDate;

    @DynamoDBAttribute(attributeName = "subscriptionId")
    private String subscriptionId;

    @DynamoDBAttribute(attributeName = "purchaseToken")
    private String purchaseToken;

    @DynamoDBAttribute(attributeName = "storeType")
    @DynamoDBTypeConvertedEnum
    private StoreType storeType;
}
