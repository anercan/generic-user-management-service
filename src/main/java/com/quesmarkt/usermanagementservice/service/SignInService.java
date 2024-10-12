package com.quesmarkt.usermanagementservice.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.quesmarkt.usermanagementservice.data.entity.LoginTransaction;
import com.quesmarkt.usermanagementservice.data.entity.User;
import com.quesmarkt.usermanagementservice.data.request.GoogleLoginRequest;
import com.quesmarkt.usermanagementservice.data.request.SignInRequest;
import com.quesmarkt.usermanagementservice.data.response.SignInResponse;
import com.quesmarkt.usermanagementservice.manager.GoogleAuthManager;
import com.quesmarkt.usermanagementservice.manager.LoginTransactionManager;
import com.quesmarkt.usermanagementservice.manager.UserManager;
import com.quesmarkt.usermanagementservice.util.JwtUtil;
import com.quesmarkt.usermanagementservice.util.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.time.ZonedDateTime;
import java.util.*;

import static com.quesmarkt.usermanagementservice.util.UserUtils.createInitialUser;

/**
 * @author anercan
 */

@Service
@AllArgsConstructor
public class SignInService extends BaseService {

    private UserManager userManager;
    private LoginTransactionManager loginTransactionManager;
    private HttpServletRequest request;
    private GoogleAuthManager googleAuthManager;

    public ResponseEntity<SignInResponse> basicSignIn(SignInRequest request) {
        //todo check for validation and handle bruteforce
        boolean isLoginSucceed = false;
        String userId = null;
        try {
            User user = userManager.getUserByMail(request.getEmail(), request.getAppId()).orElse(null);
            if (user != null) {
                userId = user.getId();
                if (isMailAndPasswordMatch(request, user)) {
                    isLoginSucceed = true;
                    String jwt = JwtUtil.createJWT(user.getId(), request.getJwtClaims(), request.getExpirationDays(), request.getAppId(), UserUtils.getUserPremiumType(user.getPremiumInfo()));
                    return ResponseEntity.ok(SignInResponse.builder().jwt(jwt).build());
                } else {
                    return ResponseEntity.ok(SignInResponse.builder().message("Wrong credentials.").status(-1).build());
                }
            } else {
                return ResponseEntity.ok(SignInResponse.builder().message("Mail not found.").status(-2).build());
            }
        } catch (Exception e) {
            isLoginSucceed = false;
            logger.error("basicSignIn got exception", e);
            return ResponseEntity.internalServerError().build();
        } finally {
            saveLoginTransaction(userId, isLoginSucceed);
        }
    }

    private void saveLoginTransaction(String userId, boolean isLoginSucceed) {
        if (StringUtils.isNotEmpty(userId)) {
            try {
                LoginTransaction loginTransaction = new LoginTransaction();
                loginTransaction.setIp(getIpAddress());
                loginTransaction.setDate(ZonedDateTime.now());
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
        GoogleIdToken.Payload payload = googleAuthManager.verifyToken(request.getToken());
        if (payload != null && BooleanUtils.isTrue(payload.getEmailVerified())) {
            boolean isLoginSucceed = false;
            String userId = null;
            try {
                User user = getOrElseInsert(payload, request.getAppId());
                userId = user.getId();
                isLoginSucceed = true;
                String jwt = JwtUtil.createJWT(userId, getJwtClaimsWithPayload(request, payload), request.getExpirationDays(), request.getAppId(), UserUtils.getUserPremiumType(user.getPremiumInfo()));
                return ResponseEntity.ok(SignInResponse.builder().jwt(jwt).build());
            } catch (Exception e) {
                isLoginSucceed = false;
                logger.error("basicSignIn got exception request:{}", request, e);
                return ResponseEntity.internalServerError().build();
            } finally {
                saveLoginTransaction(userId, isLoginSucceed);
            }
        }
        return ResponseEntity.badRequest().build();
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
            userOptional = userManager.insert(createInitialUser(payload, appId));
        }
        return userOptional.orElseThrow(() -> {
            throw new RuntimeException("user couln't found.");
        });
    }

    private boolean isMailAndPasswordMatch(SignInRequest request, User user) {
        return request.getPassword().equals(user.getPassword());
    }
}
