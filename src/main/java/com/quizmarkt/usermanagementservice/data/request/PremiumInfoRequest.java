package com.quizmarkt.usermanagementservice.data.request;

import com.quizmarkt.usermanagementservice.data.enums.PremiumType;
import lombok.Data;

import java.util.Map;

/**
 * @author anercan
 */

@Data
public class PremiumInfoRequest {
    private PremiumType premiumType;
    private String userId;
    private Map<String, String> jwtClaims;
    private int appId;
    private GoogleSubscriptionRequest googleSubscriptionRequest;
}
