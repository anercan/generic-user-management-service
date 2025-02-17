package com.quizmarkt.usermanagementservice.util;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.quizmarkt.usermanagementservice.data.entity.DeviceInfo;
import com.quizmarkt.usermanagementservice.data.entity.PremiumInfo;
import com.quizmarkt.usermanagementservice.data.entity.User;
import com.quizmarkt.usermanagementservice.data.enums.PremiumType;
import com.quizmarkt.usermanagementservice.data.enums.UserState;
import com.quizmarkt.usermanagementservice.data.request.SignInRequest;

import java.time.ZonedDateTime;

/**
 * @author anercan
 */
public class UserUtils {

    public static User createInitialUserWithGoogleLogin(SignInRequest.DeviceInfo deviceInfoRequest, GoogleIdToken.Payload payload, int appId) {
        User user = getInitialUser(deviceInfoRequest, payload, appId);
        user.setAvatarUrl((String) payload.get("picture"));
        user.setName((String) payload.get("name"));
        return user;
    }

    private static User getInitialUser(SignInRequest.DeviceInfo deviceInfoRequest, GoogleIdToken.Payload payload, int appId) {
        User user = new User();
        user.setEmail(payload.getEmail());
        user.setState(UserState.ACTIVE);
        user.setAppId(appId);
        user.setCreatedDate(ZonedDateTime.now());
        user.setDeviceInfo(getDeviceInfo(deviceInfoRequest));
        user.setPremiumInfo(getNonPremiumInfo());
        return user;
    }

    private static PremiumInfo getNonPremiumInfo() {
        PremiumInfo premiumInfo = new PremiumInfo();
        premiumInfo.setPremiumType(PremiumType.NONE);
        return premiumInfo;
    }

    public static DeviceInfo getDeviceInfo(SignInRequest.DeviceInfo deviceInfoRequest) {
        if (deviceInfoRequest == null) {
            return null;
        }
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setOsType(deviceInfoRequest.getOsType());
        deviceInfo.setFcmToken(deviceInfoRequest.getToken());
        return deviceInfo;
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
