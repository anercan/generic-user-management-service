package com.quesmarkt.usermanagementservice.service;

import com.quesmarkt.usermanagementservice.data.entity.User;
import com.quesmarkt.usermanagementservice.data.request.SignUpRequest;
import com.quesmarkt.usermanagementservice.data.response.SignUpResponse;
import com.quesmarkt.usermanagementservice.manager.UserManager;
import com.quesmarkt.usermanagementservice.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.quesmarkt.usermanagementservice.util.UserUtils.createInitialUser;

/**
 * @author anercan
 */

@Service
@AllArgsConstructor
public class SignUpService extends BaseService {

    private final UserManager userManager;

    public ResponseEntity<SignUpResponse> basicSignUp(SignUpRequest signUpRequest) {
        try {
            if (!isRequestValid(signUpRequest)) {
                return ResponseEntity.badRequest().body(SignUpResponse.builder().message("").build());
            }
            Boolean srExistByMail = userManager.isExistByEmail(signUpRequest.getMail());
            if (BooleanUtils.isTrue(srExistByMail)) {
                return ResponseEntity.badRequest().body(SignUpResponse.builder().message("").build());
            }
            User savedUser = userManager.insert(createInitialUser(signUpRequest));
            if (Objects.nonNull(savedUser)) {
                String jwt = JwtUtil.createJWT(savedUser.getId(), savedUser.getUsername());
                return ResponseEntity.ok(SignUpResponse.builder().jwt(jwt).build());
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private boolean isRequestValid(SignUpRequest signUpRequest) {
        return true; //todo validation
    }


}
