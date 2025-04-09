package com.quizmarkt.usermanagementservice.manager;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.api.services.androidpublisher.model.SubscriptionPurchase;
import com.quizmarkt.usermanagementservice.data.entity.PremiumInfo;
import com.quizmarkt.usermanagementservice.data.entity.User;
import com.quizmarkt.usermanagementservice.data.enums.PremiumType;
import com.quizmarkt.usermanagementservice.data.enums.StoreType;
import com.quizmarkt.usermanagementservice.data.mapper.SubscriptionTransactionMapper;
import com.quizmarkt.usermanagementservice.data.repository.SubscriptionTransactionRepository;
import com.quizmarkt.usermanagementservice.data.repository.UserRepository;
import com.quizmarkt.usermanagementservice.data.request.GoogleSubscriptionRequest;
import com.quizmarkt.usermanagementservice.data.request.PremiumInfoRequest;
import com.quizmarkt.usermanagementservice.data.request.UserFilterRequest;
import com.quizmarkt.usermanagementservice.manager.exception.AppException;
import com.quizmarkt.usermanagementservice.manager.exception.DataAccessException;
import com.quizmarkt.usermanagementservice.manager.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author anercan
 */

@Service
@AllArgsConstructor
public class UserManager extends BaseManager {

    private final UserRepository userRepository;
    private final SubscriptionTransactionRepository subscriptionTransactionRepository;
    private final SubscriptionTransactionMapper subscriptionTransactionMapper;
    private final DynamoDBMapper dynamoDBMapper;

    public List<User> getAllByFilter(UserFilterRequest filterDto) {
        Map<String, AttributeValue> expressionValues = new HashMap<>();
        List<String> conditions = new ArrayList<>();

        if (Objects.nonNull(filterDto.getId())) {
            conditions.add("id = :id");
            expressionValues.put(":id", new AttributeValue().withS(filterDto.getId()));
        } else if (Objects.nonNull(filterDto.getPremiumType())) {
            conditions.add("premiumInfo.premiumType = :premiumType");
            expressionValues.put(":premiumType", new AttributeValue().withS(filterDto.getPremiumType().name()));
        } else {
            LocalDate today = LocalDate.now(ZoneId.of("UTC"));
            ZonedDateTime startOfDay = today.atStartOfDay(ZoneId.of("UTC"));
            ZonedDateTime endOfDay = startOfDay.plusDays(1);

            conditions.add("createdDate BETWEEN :start AND :end");
            expressionValues.put(":start", new AttributeValue().withS(startOfDay.toString()));
            expressionValues.put(":end", new AttributeValue().withS(endOfDay.toString()));
        }

        String filterExpression = String.join(" AND ", conditions);

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression(filterExpression)
                .withExpressionAttributeValues(expressionValues);

        return dynamoDBMapper.scan(User.class, scanExpression);
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
            subscriptionTransactionRepository.save(subscriptionTransactionMapper.getSubscriptionTransactionEntity(request, googleSubscriptionPurchaseResponse));
        } catch (Exception e) {
            logger.error("saveTransaction got exception userId:{}", request.getUserId(), e);
        }
    }

    public void updateUsersPremiumInfo(User user, PremiumType premiumType) {
        if (user.getPremiumInfo() != null) {
            user.getPremiumInfo().setPremiumType(premiumType);
            logger.info("updateUsersPremiumInfo users premium info set to {}", premiumType);
            userRepository.save(user);
        }
    }
}
