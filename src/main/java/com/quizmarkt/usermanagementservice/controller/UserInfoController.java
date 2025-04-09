package com.quizmarkt.usermanagementservice.controller;

import com.quizmarkt.usermanagementservice.data.entity.User;
import com.quizmarkt.usermanagementservice.data.request.PremiumInfoRequest;
import com.quizmarkt.usermanagementservice.data.request.UserFilterRequest;
import com.quizmarkt.usermanagementservice.data.response.UpdatePremiumInfoResponse;
import com.quizmarkt.usermanagementservice.data.response.UserInfo;
import com.quizmarkt.usermanagementservice.service.SubscriptionService;
import com.quizmarkt.usermanagementservice.service.UserInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author anercan
 */

@RestController
@AllArgsConstructor
@RequestMapping("/user-info")
public class UserInfoController extends BaseController {

    private final UserInfoService userInfoService;
    private final SubscriptionService subscriptionService;

    @PostMapping("/google-play-subscribe")
    public ResponseEntity<UpdatePremiumInfoResponse> googlePlaySubscribe(@RequestBody PremiumInfoRequest request) {
        return subscriptionService.googlePlaySubscribe(request);
    }

    @PostMapping("/get-user-info")
    public ResponseEntity<UserInfo> getUserInfo(@RequestBody String userId) {
        return userInfoService.getUserInfo(userId);
    }

    @PostMapping("/get-users-filter")
    public ResponseEntity<List<User>> getUserByFilter(@RequestBody UserFilterRequest request) {
        return userInfoService.getUsersByFilter(request);
    }

}
