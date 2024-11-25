package com.quizmarkt.usermanagementservice.data.mapper;

import com.google.api.services.androidpublisher.model.SubscriptionPurchase;
import com.quizmarkt.usermanagementservice.data.entity.GoogleSubscriptionPurchaseResponse;
import com.quizmarkt.usermanagementservice.data.entity.GoogleTransactionReceipt;
import com.quizmarkt.usermanagementservice.data.request.GoogleSubscriptionRequest;
import com.quizmarkt.usermanagementservice.data.request.TransactionReceipt;
import org.mapstruct.Mapper;

/**
 * @author anercan
 */

@Mapper(componentModel = "spring")
public interface GoogleSubscriptionModelMapper {

    GoogleSubscriptionPurchaseResponse toGoogleSubscriptionPurchaseResponse(SubscriptionPurchase dto);
    com.quizmarkt.usermanagementservice.data.entity.GoogleSubscriptionRequest toGoogleSubscriptionRequest(GoogleSubscriptionRequest dto);
    GoogleTransactionReceipt toGoogleTransactionReceipt(TransactionReceipt dto);

}
