package com.quesmarkt.usermanagementservice.service;

import com.quesmarkt.usermanagementservice.data.entity.User;
import com.quesmarkt.usermanagementservice.data.request.PremiumInfoRequest;
import com.quesmarkt.usermanagementservice.data.response.UpdatePremiumInfoResponse;
import com.quesmarkt.usermanagementservice.manager.UserManager;
import com.quesmarkt.usermanagementservice.util.JwtUtil;
import com.quesmarkt.usermanagementservice.util.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author anercan
 */

@Service
@AllArgsConstructor
public class UserInfoService {

    private UserManager userManager;

    public ResponseEntity<UpdatePremiumInfoResponse> updatePremiumInfo(PremiumInfoRequest request) {
        UpdatePremiumInfoResponse response = new UpdatePremiumInfoResponse();
        try {
            User user = userManager.setUserPremiumInfo(request);
            response.setSucceed(true);
            response.setJwt(JwtUtil.createJWT(request.getUserId(),request.getJwtClaims(), request.getJwtExpireDate(), request.getAppId(), UserUtils.getUserPremiumType(user.getPremiumInfo())));
        } catch (Exception e) {
            response.setSucceed(false);
            response.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
