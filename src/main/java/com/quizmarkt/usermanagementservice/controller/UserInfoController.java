package com.quizmarkt.usermanagementservice.controller;

import com.quizmarkt.usermanagementservice.data.request.PremiumInfoRequest;
import com.quizmarkt.usermanagementservice.data.response.UpdatePremiumInfoResponse;
import com.quizmarkt.usermanagementservice.data.response.UserInfo;
import com.quizmarkt.usermanagementservice.service.UserInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author anercan
 */

@RestController
@AllArgsConstructor
@RequestMapping("/user-info")
public class UserInfoController extends BaseController {

    private final UserInfoService userInfoService;

    @PostMapping("/google-play-subscribe")
    public ResponseEntity<UpdatePremiumInfoResponse> googlePlaySubscribe(@RequestBody PremiumInfoRequest request) {
        return userInfoService.googlePlaySubscribe(request);
    }

    @PostMapping("/get-user-info")
    public ResponseEntity<UserInfo> getUserInfo(@RequestBody String userId) {
        return userInfoService.getUserInfo(userId);
    }

}
