package com.quesmarkt.usermanagementservice.data.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.quesmarkt.usermanagementservice.data.converter.ZonedDateTypeConverter;
import com.quesmarkt.usermanagementservice.data.enums.PremiumType;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * @author anercan
 */

@Data
@DynamoDBDocument
public class PremiumInfo {

    @DynamoDBAttribute(attributeName = "premiumType")
    @DynamoDBTypeConvertedEnum
    private PremiumType premiumType = PremiumType.NONE;

    @DynamoDBTypeConverted(converter = ZonedDateTypeConverter.class)
    @DynamoDBAttribute(attributeName = "expireDate")
    private ZonedDateTime expireDate;
}
