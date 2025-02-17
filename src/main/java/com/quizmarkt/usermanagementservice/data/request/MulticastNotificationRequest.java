package com.quizmarkt.usermanagementservice.data.request;

import lombok.Data;

import java.util.List;

/**
 * @author anercan
 */

@Data
public class MulticastNotificationRequest {
    private String title;
    private String body;
    private List<String> userIDList;
    private Integer appId;
}
