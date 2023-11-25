package com.quesmarkt.usermanagementservice.service;

import com.quesmarkt.usermanagementservice.data.entity.LoginTransaction;
import com.quesmarkt.usermanagementservice.data.entity.User;
import com.quesmarkt.usermanagementservice.data.request.SignInRequest;
import com.quesmarkt.usermanagementservice.data.response.SignInResponse;
import com.quesmarkt.usermanagementservice.manager.LoginTransactionManager;
import com.quesmarkt.usermanagementservice.manager.UserManager;
import com.quesmarkt.usermanagementservice.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.TimeZone;

/**
 * @author anercan
 */

@Service
@AllArgsConstructor
public class SignInService extends BaseService {

    private UserManager userManager;
    private LoginTransactionManager loginTransactionManager;
    private HttpServletRequest request;

    public ResponseEntity<SignInResponse> basicSignIn(SignInRequest request) {
        //todo check for validation and handle bruteforce
        boolean isLoginSucceed = false;
        String userId = null;
        try {
            User user = userManager.getUserByMail(request.getEmail());
            userId = user.getId();
            if (isMailAndPasswordMatch(request, user)) {
                isLoginSucceed = true;
                String jwt = JwtUtil.createJWT(user.getId(), user.getUsername(), 30L);
                return ResponseEntity.ok(SignInResponse.builder().jwt(jwt).build());
            } else {
                return ResponseEntity.ok(SignInResponse.builder().message("basicSignIn.mailNotFound").status(-1).build());
            }
        } catch (Exception e) {
            isLoginSucceed = false;
            logger.error("basicSignIn got exception", e);
            return ResponseEntity.badRequest().build();
        } finally {
            saveLoginTransaction(userId, isLoginSucceed);
        }
    }

    private void saveLoginTransaction(String userId, boolean isLoginSucceed) {
        try {
            LoginTransaction loginTransaction = new LoginTransaction();
            loginTransaction.setIp(getIpAddress());
            loginTransaction.setDate(LocalDateTime.now());
            loginTransaction.setLoginSucceed(isLoginSucceed);
            TimeZone timeZone = RequestContextUtils.getTimeZone(request);
            loginTransaction.setZone(Objects.nonNull(timeZone) ? timeZone.getDisplayName() : null); // todo use third party for zone info
            loginTransaction.setUserId(userId);
            loginTransactionManager.saveNewLoginTransaction(loginTransaction);
        } catch (Exception e) {
            logger.error("saveLoginTransaction got exception ", e);
        }

    }

    private String getIpAddress() {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    private boolean isMailAndPasswordMatch(SignInRequest request, User user) {
        return request.getPassword().equals(user.getPassword());
    }
}
