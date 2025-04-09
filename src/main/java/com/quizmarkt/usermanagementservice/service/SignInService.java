package com.quizmarkt.usermanagementservice.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.services.androidpublisher.model.SubscriptionPurchase;
import com.quizmarkt.usermanagementservice.data.entity.LoginTransaction;
import com.quizmarkt.usermanagementservice.data.entity.User;
import com.quizmarkt.usermanagementservice.data.enums.PremiumType;
import com.quizmarkt.usermanagementservice.data.request.GoogleLoginRequest;
import com.quizmarkt.usermanagementservice.data.request.SignInRequest;
import com.quizmarkt.usermanagementservice.data.response.SignInResponse;
import com.quizmarkt.usermanagementservice.manager.GoogleAuthManager;
import com.quizmarkt.usermanagementservice.manager.GooglePlaySubscriptionManager;
import com.quizmarkt.usermanagementservice.manager.LoginTransactionManager;
import com.quizmarkt.usermanagementservice.manager.UserManager;
import com.quizmarkt.usermanagementservice.util.JwtUtil;
import com.quizmarkt.usermanagementservice.util.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author anercan
 */

@Service
@AllArgsConstructor
@Slf4j
public class SignInService {

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
                log.error("saveLoginTransaction got exception ", e);
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
                User user = getOrElseInsert(request.getDeviceInfo(),payload, request.getAppId());
                if (UserUtils.isPremiumUser(user.getPremiumInfo())) {
                    updatePremiumInfos(user);
                    getJWTExpireDateForPremiumUser(user).ifPresent(request::setExpirationDate);
                }
                userId = user.getId();
                String jwt = JwtUtil.createJWT(userId, getJwtClaimsWithPayload(request, payload), request.getExpirationDate(), request.getAppId(), UserUtils.getUserPremiumType(user.getPremiumInfo()));
                return ResponseEntity.ok(SignInResponse.builder().jwt(jwt).build());
            } catch (Exception e) {
                isLoginSucceed = false;
                log.error("googleSignIn got exception request:{}", request.getEmail(), e);
                return ResponseEntity.internalServerError().build();
            } finally {
                saveLoginTransaction(userId, isLoginSucceed, request.getAppId());
            }
        } else {
            log.error("googleSignIn error verifyToken failed token:{}", request.getToken());
            return ResponseEntity.badRequest().build();
        }
    }

    private void updatePremiumInfos(User user) {
        SubscriptionPurchase subscriptionPurchase = googlePlaySubscriptionManager.getSubscriptionData(user.getPremiumInfo().getSubscriptionId(), user.getPremiumInfo().getPurchaseToken(), user.getId(), user.getAppId());
        if (subscriptionPurchase != null && !Objects.equals(subscriptionPurchase.getExpiryTimeMillis(), user.getPremiumInfo().getExpireDate())) {
            user.getPremiumInfo().setExpireDate(subscriptionPurchase.getExpiryTimeMillis());
            log.info("Subscription renew detected expire time will update.userId:{}", user.getId());
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
            return new HashMap<>();
        }
        return request.getJwtClaims();
    }

    private User getOrElseInsert(SignInRequest.DeviceInfo deviceInfoRequest, GoogleIdToken.Payload payload, int appId) {
        Optional<User> userOptional = userManager.getUserByMail(payload.getEmail(), appId);
        if (userOptional.isEmpty() && BooleanUtils.isTrue(payload.getEmailVerified())) {
            User initialUserWithGoogleLogin = UserUtils.createInitialUserWithGoogleLogin(deviceInfoRequest, payload, appId);
            userOptional = userManager.save(initialUserWithGoogleLogin);
            log.info("New user created for appId:{} mail:{}", appId, initialUserWithGoogleLogin.getEmail());
        }
        User user = userOptional.orElseThrow(() -> {throw new RuntimeException("User couldn't found.");});
        checkAndUpdateDeviceInfo(deviceInfoRequest, user);
        return user;
    }

    private void checkAndUpdateDeviceInfo(SignInRequest.DeviceInfo deviceInfoRequest, User user) {
        try {
            boolean isDeviceInfoExistInRequest = deviceInfoRequest != null && StringUtils.isNotEmpty(deviceInfoRequest.getToken());
            if (isDeviceInfoExistInRequest) {
                boolean updateNeedForToken = user.getDeviceInfo() == null || !deviceInfoRequest.getToken().equals(user.getDeviceInfo().getFcmToken());
                if (updateNeedForToken) {
                    user.setDeviceInfo(UserUtils.getDeviceInfo(deviceInfoRequest));
                    log.info("Device info updated for user:{}", user.getId());
                    userManager.save(user);
                }
            }
        } catch (Exception e) {
            log.error("checkAndUpdateDeviceInfo got exception {} user:{} deviceInfo{} req:{}", e, user.getId(), user.getDeviceInfo(), deviceInfoRequest);
        }

    }

    public ResponseEntity<SignInResponse> adminSignIn(SignInRequest request) {
        if (UserUtils.verifyAdminLogin(request)) {
            try {
                String jwt = JwtUtil.createJWT("null", request.getJwtClaims(), Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()),1, PremiumType.LEVEL1);
                return ResponseEntity.ok(SignInResponse.builder().jwt(jwt).build());
            } catch (Exception e) {
                log.error("adminSignIn got exception request:{}", request, e);
                return ResponseEntity.internalServerError().build();
            }
        } else {
            log.error("adminSignIn error invalid credentials user:{}", request.getEmail());
            return ResponseEntity.badRequest().build();
        }
    }
}
