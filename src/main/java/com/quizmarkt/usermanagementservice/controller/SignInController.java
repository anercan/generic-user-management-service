package com.quizmarkt.usermanagementservice.controller;

import com.quizmarkt.usermanagementservice.data.request.GoogleLoginRequest;
import com.quizmarkt.usermanagementservice.data.response.SignInResponse;
import com.quizmarkt.usermanagementservice.service.SignInService;
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
@RequestMapping("/sign-in")
public class SignInController {

    private final SignInService signInService;

    @PostMapping("/google-auth")
    public ResponseEntity<SignInResponse> googleSignIn(@RequestBody GoogleLoginRequest request) {
        return signInService.googleSignIn(request);
    }
}