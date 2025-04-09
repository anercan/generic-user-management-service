package com.quizmarkt.usermanagementservice.data.mapper;

import com.google.api.services.androidpublisher.model.SubscriptionPurchase;
import com.quizmarkt.usermanagementservice.data.entity.SubscriptionTransaction;
import com.quizmarkt.usermanagementservice.data.request.PremiumInfoRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author anercan
 */

@Component
@AllArgsConstructor
public class SubscriptionTransactionMapper {

    private final GoogleSubscriptionModelMapper googleSubscriptionModelMapper;

    public SubscriptionTransaction getSubscriptionTransactionEntity(PremiumInfoRequest request, SubscriptionPurchase googleSubscriptionPurchaseResponse) {
        SubscriptionTransaction entity = new SubscriptionTransaction();
        entity.setUserId(request.getUserId());
        entity.setAppId(request.getAppId());
        entity.setGoogleSubscriptionRequest(googleSubscriptionModelMapper.toGoogleSubscriptionRequest(request.getGoogleSubscriptionRequest()));
        entity.setGoogleSubscriptionPurchaseResponse(googleSubscriptionModelMapper.toGoogleSubscriptionPurchaseResponse(googleSubscriptionPurchaseResponse));
        return entity;
    }
}
