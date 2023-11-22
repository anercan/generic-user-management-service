package com.quesmarkt.usermanagementservice.service;

import com.quesmarkt.usermanagementservice.data.entity.User;
import com.quesmarkt.usermanagementservice.data.request.SignInRequest;
import com.quesmarkt.usermanagementservice.data.response.SignInResponse;
import com.quesmarkt.usermanagementservice.manager.UserManager;
import com.quesmarkt.usermanagementservice.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author anercan
 */

@Service
@AllArgsConstructor
public class SignInService extends BaseService {

    private UserManager userManager;

    public ResponseEntity<SignInResponse> basicSignIn(SignInRequest request) {
        try {
            User user = userManager.getUserByMail(request.getEmail());
            if (request.getPassword().equals(user.getPassword())) {
                String jwt = JwtUtil.createJWT(user.getId(), user.getUsername(), 30L);
                return ResponseEntity.ok(SignInResponse.builder().jwt(jwt).build());
            } else {
                return ResponseEntity.ok(SignInResponse.builder().message("basicSignIn.mailNotFound").build());
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
