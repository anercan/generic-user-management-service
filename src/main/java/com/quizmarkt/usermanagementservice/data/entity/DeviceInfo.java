package com.quizmarkt.usermanagementservice.data.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.quizmarkt.usermanagementservice.data.enums.OSType;
import lombok.Data;

/**
 * @author anercan
 */

@Data
@DynamoDBDocument
public class DeviceInfo {

    @DynamoDBAttribute(attributeName = "fcmToken")
    private String fcmToken;

    @DynamoDBAttribute(attributeName = "osType")
    @DynamoDBTypeConvertedEnum
    private OSType osType;

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "fcmToken='" + fcmToken + '\'' +
                ", osType=" + osType +
                '}';
    }
}
