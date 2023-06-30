package com.quesmarkt.usermanagementservice.service;

import com.quesmarkt.usermanagementservice.data.entity.User;
import com.quesmarkt.usermanagementservice.data.request.SignUpRequest;
import com.quesmarkt.usermanagementservice.data.response.ServiceResponse;
import com.quesmarkt.usermanagementservice.data.response.SignUpResponse;
import com.quesmarkt.usermanagementservice.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import static com.quesmarkt.usermanagementservice.util.UserUtils.createInitialUser;

/**
 * @author anercan
 */

@Service
@AllArgsConstructor
public class UserManagementService extends BaseService {

    private final UserService userService;

    public ServiceResponse<SignUpResponse> basicSignUp(SignUpRequest signUpRequest) {
        ServiceResponse<Boolean> srExistByMail = userService.isExistByEmail(signUpRequest.getEmail());
        if (isSucceed(srExistByMail) && BooleanUtils.isTrue(srExistByMail.getValue())) {
            return null; //todo
        }
        User initUser = createInitialUser(signUpRequest);

        ServiceResponse<User> insertResult = userService.insert(initUser);

        if (isSucceed(insertResult) && insertResult.getValue() != null) {
            User savedUser = insertResult.getValue();
            return new ServiceResponse<>(
                    SignUpResponse.builder()
                            .jwt(JwtUtil.createJWT(savedUser.getId(), savedUser.getUsername()))
                            .build());
        }

        return createFailResult("Failed.");
    }


}
