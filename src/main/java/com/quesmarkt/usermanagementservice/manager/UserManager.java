package com.quesmarkt.usermanagementservice.manager;

import com.google.api.services.androidpublisher.model.SubscriptionPurchase;
import com.quesmarkt.usermanagementservice.data.entity.PremiumInfo;
import com.quesmarkt.usermanagementservice.data.entity.SubscriptionTransaction;
import com.quesmarkt.usermanagementservice.data.entity.User;
import com.quesmarkt.usermanagementservice.data.enums.PremiumType;
import com.quesmarkt.usermanagementservice.data.enums.StoreType;
import com.quesmarkt.usermanagementservice.data.mapper.GoogleSubscriptionModelMapper;
import com.quesmarkt.usermanagementservice.data.repository.SubscriptionTransactionRepository;
import com.quesmarkt.usermanagementservice.data.repository.UserRepository;
import com.quesmarkt.usermanagementservice.data.request.GoogleSubscriptionRequest;
import com.quesmarkt.usermanagementservice.data.request.PremiumInfoRequest;
import com.quesmarkt.usermanagementservice.manager.exception.AppException;
import com.quesmarkt.usermanagementservice.manager.exception.DataAccessException;
import com.quesmarkt.usermanagementservice.manager.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author anercan
 */

@Service
@AllArgsConstructor
public class UserManager extends BaseManager {

    private final UserRepository userRepository;
    private final SubscriptionTransactionRepository subscriptionTransactionRepository;
    private final GoogleSubscriptionModelMapper googleSubscriptionModelMapper;

    public Boolean isExistByEmail(String email) {
        try {
            return userRepository.existsByEmail(email);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    public Optional<User> insert(User user) {
        try {
            return Optional.of(userRepository.save(user));
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    public Optional<User> getUserByMail(String mail, Integer appId) {
        try {
            return userRepository.findByEmailAndAppId(mail, appId);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    public User setUserPremiumInfo(PremiumInfoRequest request, SubscriptionPurchase googleSubscriptionPurchaseResponse) {
        Optional<User> userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            logger.warn("setUserPremiumInfo - User couldn't found! userID:{}", request.getUserId());
            throw new UserNotFoundException("User couldn't found!");
        }
        try {
            User user = userOpt.get();
            PremiumInfo premiumInfo = getPremiumInfo(request, googleSubscriptionPurchaseResponse);
            user.setPremiumInfo(premiumInfo);
            userRepository.save(user);
            logger.info("User premium info updated! userID:{}", request.getUserId());
            return user;
        } catch (Exception e) {
            logger.error("User premium info got exception! userID:{}", request.getUserId());
            throw new AppException(e);
        } finally {
            saveSubscriptionTransactionTransaction(request, googleSubscriptionPurchaseResponse);
        }
    }

    private PremiumInfo getPremiumInfo(PremiumInfoRequest request, SubscriptionPurchase googleSubscriptionPurchaseResponse) {
        PremiumInfo premiumInfo = new PremiumInfo();
        premiumInfo.setPremiumType(request.getPremiumType());
        premiumInfo.setStoreType(StoreType.GOOGLE_PLAY);
        GoogleSubscriptionRequest googleSubscriptionRequest = request.getGoogleSubscriptionRequest();
        premiumInfo.setPurchaseToken(googleSubscriptionRequest.getPurchaseToken());
        premiumInfo.setSubscriptionId(googleSubscriptionRequest.getProductId());
        premiumInfo.setExpireDate(googleSubscriptionPurchaseResponse.getExpiryTimeMillis());
        return premiumInfo;
    }

    private void saveSubscriptionTransactionTransaction(PremiumInfoRequest request, SubscriptionPurchase googleSubscriptionPurchaseResponse) {
        try {
            subscriptionTransactionRepository.save(getSubscriptionTransactionEntity(request, googleSubscriptionPurchaseResponse));
        } catch (Exception e) {
            logger.error("saveTransaction got exception userId:{}", request.getUserId(), e);
        }
    }

    private SubscriptionTransaction getSubscriptionTransactionEntity(PremiumInfoRequest request, SubscriptionPurchase googleSubscriptionPurchaseResponse) {
        SubscriptionTransaction entity = new SubscriptionTransaction();
        entity.setUserId(request.getUserId());
        entity.setAppId(request.getAppId());
        entity.setGoogleSubscriptionRequest(googleSubscriptionModelMapper.toGoogleSubscriptionRequest(request.getGoogleSubscriptionRequest()));
        entity.setGoogleSubscriptionPurchaseResponse(googleSubscriptionModelMapper.toGoogleSubscriptionPurchaseResponse(googleSubscriptionPurchaseResponse));
        return entity;
    }

    public void updateUsersPremiumInfo(User user, PremiumType premiumType) {
        if (user.getPremiumInfo() != null) {
            user.getPremiumInfo().setPremiumType(premiumType);
            logger.info("updateUsersPremiumInfo users premium info set to {}", premiumType);
            userRepository.save(user);
        }
    }
}
