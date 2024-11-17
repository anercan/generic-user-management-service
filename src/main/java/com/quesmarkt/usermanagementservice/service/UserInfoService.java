package com.quesmarkt.usermanagementservice.service;

import com.google.api.services.androidpublisher.model.SubscriptionPurchase;
import com.quesmarkt.usermanagementservice.data.entity.User;
import com.quesmarkt.usermanagementservice.data.request.GoogleSubscriptionRequest;
import com.quesmarkt.usermanagementservice.data.request.PremiumInfoRequest;
import com.quesmarkt.usermanagementservice.data.response.UpdatePremiumInfoResponse;
import com.quesmarkt.usermanagementservice.manager.GooglePlaySubscriptionManager;
import com.quesmarkt.usermanagementservice.manager.UserManager;
import com.quesmarkt.usermanagementservice.util.JwtUtil;
import com.quesmarkt.usermanagementservice.util.UserUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author anercan
 */

@Service
@AllArgsConstructor
@Slf4j
public class UserInfoService {

    private UserManager userManager;
    private GooglePlaySubscriptionManager googlePlaySubscriptionManager;

    public ResponseEntity<UpdatePremiumInfoResponse> googlePlaySubscribe(PremiumInfoRequest request) {
        GoogleSubscriptionRequest googleSubscriptionRequest = request.getGoogleSubscriptionRequest();
        SubscriptionPurchase subscriptionPurchase = googlePlaySubscriptionManager.getSubscriptionData(googleSubscriptionRequest.getProductId(), googleSubscriptionRequest.getTransactionReceipt().getPurchaseToken(), request.getUserId());
        if (googlePlaySubscriptionManager.isSubscriptionValid(subscriptionPurchase)) {
            UpdatePremiumInfoResponse response = new UpdatePremiumInfoResponse();
            try {
                User user = userManager.setUserPremiumInfo(request, subscriptionPurchase);
                response.setSucceed(true);
                response.setJwt(
                        JwtUtil.createJWT(
                                request.getUserId(),
                                request.getJwtClaims(),
                                new Date(subscriptionPurchase.getExpiryTimeMillis()),
                                request.getAppId(),
                                UserUtils.getUserPremiumType(user.getPremiumInfo())
                        )
                );
            } catch (Exception e) {
                response.setSucceed(false);
                response.setMessage(e.getMessage());
            }
            return ResponseEntity.ok(response);
        } else {
            log.error("isSubscriptionValid got error userId:{} subscriptionPurchase:{}",request.getUserId(),subscriptionPurchase);
            return ResponseEntity.internalServerError().build();
        }
    }


}
