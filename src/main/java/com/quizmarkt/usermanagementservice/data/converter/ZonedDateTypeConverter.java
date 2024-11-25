package com.quizmarkt.usermanagementservice.data.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.ZonedDateTime;

public class ZonedDateTypeConverter implements DynamoDBTypeConverter<String, ZonedDateTime> {

    @Override
    public String convert(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toString();
    }

    @Override
    public ZonedDateTime unconvert(String s) {
        return ZonedDateTime.parse(s);
    }
}