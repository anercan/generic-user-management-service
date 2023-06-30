package com.quesmarkt.usermanagementservice.controller;

import com.quesmarkt.usermanagementservice.data.request.SignUpRequest;
import com.quesmarkt.usermanagementservice.data.response.SignUpResponse;
import com.quesmarkt.usermanagementservice.service.UserManagementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author anercan
 */

@Controller
@AllArgsConstructor
@RequestMapping("/sign-up")
public class SignUpController extends BaseController {

    private final UserManagementService userManagementService;

    @PostMapping
    public ResponseEntity<SignUpResponse> signUp(SignUpRequest request) {
        return createResponseEntity(userManagementService.basicSignUp(request));
    }

}
