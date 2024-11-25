package com.quizmarkt.usermanagementservice.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.services.androidpublisher.model.SubscriptionPurchase;
import com.quizmarkt.usermanagementservice.data.entity.LoginTransaction;
import com.quizmarkt.usermanagementservice.data.entity.User;
import com.quizmarkt.usermanagementservice.data.enums.PremiumType;
import com.quizmarkt.usermanagementservice.data.request.GoogleLoginRequest;
import com.quizmarkt.usermanagementservice.data.response.SignInResponse;
import com.quizmarkt.usermanagementservice.manager.GoogleAuthManager;
import com.quizmarkt.usermanagementservice.manager.GooglePlaySubscriptionManager;
import com.quizmarkt.usermanagementservice.manager.LoginTransactionManager;
import com.quizmarkt.usermanagementservice.manager.UserManager;
import com.quizmarkt.usermanagementservice.util.JwtUtil;
import com.quizmarkt.usermanagementservice.util.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author anercan
 */

@Service
@AllArgsConstructor
public class SignInService extends BaseService {

    private final UserManager userManager;
    private final LoginTransactionManager loginTransactionManager;
    private HttpServletRequest request;
    private final GoogleAuthManager googleAuthManager;
    private final GooglePlaySubscriptionManager googlePlaySubscriptionManager;

    private void saveLoginTransaction(String userId, boolean isLoginSucceed, Integer appId) {
        if (StringUtils.isNotEmpty(userId)) {
            try {
                LoginTransaction loginTransaction = new LoginTransaction();
                loginTransaction.setIp(getIpAddress());
                loginTransaction.setDate(ZonedDateTime.now());
                loginTransaction.setAppId(appId);
                loginTransaction.setLoginSucceed(isLoginSucceed);
                TimeZone timeZone = RequestContextUtils.getTimeZone(request);
                loginTransaction.setZone(Objects.nonNull(timeZone) ? timeZone.getDisplayName() : null); // todo use third party for zone info
                loginTransaction.setUserId(userId);
                loginTransactionManager.saveNewLoginTransaction(loginTransaction);
            } catch (Exception e) {
                logger.error("saveLoginTransaction got exception ", e);
            }
        }
    }

    private String getIpAddress() {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    public ResponseEntity<SignInResponse> googleSignIn(GoogleLoginRequest request) {
        boolean isLoginSucceed = true;
        GoogleIdToken.Payload payload = googleAuthManager.verifyToken(request.getToken());
        if (payload != null && BooleanUtils.isTrue(payload.getEmailVerified())) {
            String userId = null;
            try {
                User user = getOrElseInsert(payload, request.getAppId());
                if (UserUtils.isPremiumUser(user.getPremiumInfo())) {
                    updatePremiumInfos(user);
                    getJWTExpireDateForPremiumUser(user).ifPresent(request::setExpirationDate);
                }
                userId = user.getId();
                String jwt = JwtUtil.createJWT(userId, getJwtClaimsWithPayload(request, payload), request.getExpirationDate(), request.getAppId(), UserUtils.getUserPremiumType(user.getPremiumInfo()));
                return ResponseEntity.ok(SignInResponse.builder().jwt(jwt).build());
            } catch (Exception e) {
                isLoginSucceed = false;
                logger.error("basicSignIn got exception request:{}", request, e);
                return ResponseEntity.internalServerError().build();
            } finally {
                saveLoginTransaction(userId, isLoginSucceed, request.getAppId());
            }
        } else {
            logger.error("googleSignIn error verifyToken failed token:{}", request.getToken());
            return ResponseEntity.badRequest().build();
        }
    }

    private void updatePremiumInfos(User user) {
        SubscriptionPurchase subscriptionPurchase = googlePlaySubscriptionManager.getSubscriptionData(user.getPremiumInfo().getSubscriptionId(), user.getPremiumInfo().getPurchaseToken(), user.getId());
        if (subscriptionPurchase != null && !Objects.equals(subscriptionPurchase.getExpiryTimeMillis(), user.getPremiumInfo().getExpireDate())) {
            user.getPremiumInfo().setExpireDate(subscriptionPurchase.getExpiryTimeMillis());
            logger.info("Subscription renew detected expire time will update.userId:{}", user.getId());
        }
        if (UserUtils.hasSubscriptionExpired(user)) {
            userManager.updateUsersPremiumInfo(user, PremiumType.NONE);
        }
    }

    private Optional<Date> getJWTExpireDateForPremiumUser(User user) {
        if (!UserUtils.hasSubscriptionExpired(user)) {
            return Optional.of(new Date(user.getPremiumInfo().getExpireDate()));
        }
        return Optional.empty();
    }

    private Map<String, String> getJwtClaimsWithPayload(GoogleLoginRequest request, GoogleIdToken.Payload payload) {
        if (request.getJwtClaims() == null) {
            request.setJwtClaims(new HashMap<>());
            request.getJwtClaims().put("avatar", (String) payload.get("picture"));
        } else {
            request.getJwtClaims().put("avatar", (String) payload.get("picture"));
        }
        return request.getJwtClaims();
    }

    private User getOrElseInsert(GoogleIdToken.Payload payload, int appId) {
        Optional<User> userOptional = userManager.getUserByMail(payload.getEmail(), appId);
        if (userOptional.isEmpty() && BooleanUtils.isTrue(payload.getEmailVerified())) {
            User initialUserWithGoogleLogin = UserUtils.createInitialUserWithGoogleLogin(payload, appId);
            userOptional = userManager.insert(initialUserWithGoogleLogin);
            logger.info("New user created for appId:{} mail:{}", appId, initialUserWithGoogleLogin.getEmail());
        }
        return userOptional.orElseThrow(() -> {
            throw new RuntimeException("User couln't found.");
        });
    }
}
