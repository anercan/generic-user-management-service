package com.quesmarkt.usermanagementservice.service;

import com.quesmarkt.usermanagementservice.data.request.SignUpRequest;
import com.quesmarkt.usermanagementservice.data.response.ServiceResponse;
import com.quesmarkt.usermanagementservice.data.response.SignUpResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author anercan
 */

@Service
@AllArgsConstructor
public class UserManagementService {

    private final UserService userService;

    public ServiceResponse<SignUpResponse> basicSignUp(SignUpRequest signUpRequest) {
        if (userService.isExistByEmail(signUpRequest.getEmail()).getValue()){
            return null;//todo
        }
        return null;
    }

}
