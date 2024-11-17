package com.quesmarkt.usermanagementservice.controller;

import com.quesmarkt.usermanagementservice.data.request.PremiumInfoRequest;
import com.quesmarkt.usermanagementservice.data.response.UpdatePremiumInfoResponse;
import com.quesmarkt.usermanagementservice.service.UserInfoService;
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

}
