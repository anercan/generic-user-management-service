package com.quizmarkt.usermanagementservice.manager;

import com.google.api.services.androidpublisher.model.SubscriptionPurchase;
import com.quizmarkt.usermanagementservice.data.entity.PremiumInfo;
import com.quizmarkt.usermanagementservice.data.entity.SubscriptionTransaction;
import com.quizmarkt.usermanagementservice.data.entity.User;
import com.quizmarkt.usermanagementservice.data.enums.PremiumType;
import com.quizmarkt.usermanagementservice.data.enums.StoreType;
import com.quizmarkt.usermanagementservice.data.mapper.GoogleSubscriptionModelMapper;
import com.quizmarkt.usermanagementservice.data.repository.SubscriptionTransactionRepository;
import com.quizmarkt.usermanagementservice.data.repository.UserRepository;
import com.quizmarkt.usermanagementservice.data.request.GoogleSubscriptionRequest;
import com.quizmarkt.usermanagementservice.data.request.PremiumInfoRequest;
import com.quizmarkt.usermanagementservice.manager.exception.AppException;
import com.quizmarkt.usermanagementservice.manager.exception.DataAccessException;
import com.quizmarkt.usermanagementservice.manager.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

    public Optional<User> save(User user) {
        try {
            return Optional.of(userRepository.save(user));
        } catch (Exception e) {
            logger.error("save got exception", e);
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

    public Optional<User> getUserById(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw new AppException("userId can't be null or empty.");
        }
        try {
            return userRepository.findById(userId);
        } catch (Exception e) {
            logger.error("getUserById got exception", e);
            throw new DataAccessException(e);
        }
    }

    public User setUserPremiumInfo(PremiumInfoRequest request, SubscriptionPurchase googleSubscriptionPurchaseResponse) {
        try {
            Optional<User> userOpt = getUserById(request.getUserId());
            if (userOpt.isEmpty()) {
                logger.warn("setUserPremiumInfo - User couldn't found! userID:{}", request.getUserId());
                throw new UserNotFoundException("User couldn't found!");
            }
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
