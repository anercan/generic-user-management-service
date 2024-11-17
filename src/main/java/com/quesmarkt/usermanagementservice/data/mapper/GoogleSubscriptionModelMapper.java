package com.quesmarkt.usermanagementservice.data.mapper;

import com.google.api.services.androidpublisher.model.SubscriptionPurchase;
import com.quesmarkt.usermanagementservice.data.entity.GoogleSubscriptionPurchaseResponse;
import com.quesmarkt.usermanagementservice.data.entity.GoogleTransactionReceipt;
import com.quesmarkt.usermanagementservice.data.request.GoogleSubscriptionRequest;
import com.quesmarkt.usermanagementservice.data.request.TransactionReceipt;
import org.mapstruct.Mapper;

/**
 * @author anercan
 */

@Mapper(componentModel = "spring")
public interface GoogleSubscriptionModelMapper {

    GoogleSubscriptionPurchaseResponse toGoogleSubscriptionPurchaseResponse(SubscriptionPurchase dto);
    com.quesmarkt.usermanagementservice.data.entity.GoogleSubscriptionRequest toGoogleSubscriptionRequest(GoogleSubscriptionRequest dto);
    GoogleTransactionReceipt toGoogleTransactionReceipt(TransactionReceipt dto);

}
