package com.quizmarkt.usermanagementservice.service;

import com.quizmarkt.usermanagementservice.data.entity.User;
import com.quizmarkt.usermanagementservice.data.request.SignUpRequest;
import com.quizmarkt.usermanagementservice.data.response.SignUpResponse;
import com.quizmarkt.usermanagementservice.manager.UserManager;
import com.quizmarkt.usermanagementservice.util.JwtUtil;
import com.quizmarkt.usermanagementservice.util.UserUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
                return ResponseEntity.badRequest().body(SignUpResponse.builder().message("Invalid Request.").build());
            }
            Boolean srExistByMail = userManager.isExistByEmail(signUpRequest.getMail());
            if (BooleanUtils.isTrue(srExistByMail)) {
                return ResponseEntity.ok().body(SignUpResponse.builder().message("User has registered already.").status(-1).build());
            }
            Optional<User> savedUserOpt = userManager.insert(UserUtils.createInitialUserWithGoogleLogin(signUpRequest));
            if (savedUserOpt.isPresent()) {
                User savedUser = savedUserOpt.get();
                String jwt = JwtUtil.createJWT(savedUser.getId(), signUpRequest.getJwtClaims(), signUpRequest.getExpirationDate(), signUpRequest.getAppId(), UserUtils.getUserPremiumType(savedUser.getPremiumInfo()));
                return ResponseEntity.ok(SignUpResponse.builder().jwt(jwt).build());
            } else {
                return ResponseEntity.internalServerError().build();
            }
        } catch (Exception e) {
            logger.error("basicSignUp got exception", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean isRequestValid(SignUpRequest signUpRequest) {
        return true; //todo validation
    }


}
