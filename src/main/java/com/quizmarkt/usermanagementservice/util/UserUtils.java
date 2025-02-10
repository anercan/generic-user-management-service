package com.quizmarkt.usermanagementservice.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.quizmarkt.usermanagementservice.data.entity.PremiumInfo;
import com.quizmarkt.usermanagementservice.data.entity.User;
import com.quizmarkt.usermanagementservice.data.enums.PremiumType;
import com.quizmarkt.usermanagementservice.data.enums.UserState;
import com.quizmarkt.usermanagementservice.data.request.SignInRequest;
import com.quizmarkt.usermanagementservice.data.request.SignUpRequest;
import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * @author anercan
 */
public class UserUtils {

    public static User createInitialUserWithGoogleLogin(SignUpRequest signUpRequest) {
        User user = new User();
        user.setEmail(signUpRequest.getMail());
        boolean isPasswordSet = Objects.nonNull(signUpRequest.getPassword());
        user.setPassword(isPasswordSet ? signUpRequest.getPassword() : UUID.randomUUID().toString());
        user.setState(UserState.ACTIVE);
        user.setCreatedDate(ZonedDateTime.now());
        user.setUsername(StringUtils.isEmpty(signUpRequest.getUsername()) ? StringUtils.substringBefore(signUpRequest.getMail(), "@") : signUpRequest.getUsername());
        return user;
    }

    public static User createInitialUserWithGoogleLogin(GoogleIdToken.Payload payload, int appId) {
        User user = new User();
        user.setEmail(payload.getEmail());
        user.setState(UserState.ACTIVE);
        user.setAppId(appId);
        user.setCreatedDate(ZonedDateTime.now());
        user.setAvatarUrl((String) payload.get("picture"));
        user.setName((String) payload.get("name"));
        PremiumInfo premiumInfo = new PremiumInfo();
        premiumInfo.setPremiumType(PremiumType.NONE);
        user.setPremiumInfo(premiumInfo);
        return user;
    }

    public static boolean isPremiumUser(PremiumInfo premiumInfo) {
        return !PremiumType.NONE.equals(getUserPremiumType(premiumInfo));
    }

    public static PremiumType getUserPremiumType(PremiumInfo premiumInfo) {
        if (premiumInfo == null) {
            return PremiumType.NONE;
        }
        return premiumInfo.getPremiumType();
    }

    public static boolean hasSubscriptionExpired(User user) {
        return user.getPremiumInfo() != null && user.getPremiumInfo().getExpireDate() < System.currentTimeMillis();
    }

    public static boolean verifyAdminLogin(SignInRequest request) {
        String adminPw = System.getenv("ADMIN_PW");
        return request.getPassword().equals(adminPw) && "admin".equals(request.getEmail());
    }
}
