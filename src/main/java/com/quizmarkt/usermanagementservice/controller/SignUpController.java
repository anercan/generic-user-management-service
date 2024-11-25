package com.quizmarkt.usermanagementservice.controller;

import com.quizmarkt.usermanagementservice.data.request.SignUpRequest;
import com.quizmarkt.usermanagementservice.data.response.SignUpResponse;
import com.quizmarkt.usermanagementservice.service.SignUpService;
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
@RequestMapping("/sign-up")
public class SignUpController extends BaseController {

    private final SignUpService signUpService;

    @PostMapping
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest request) {
        return signUpService.basicSignUp(request);
    }

}
