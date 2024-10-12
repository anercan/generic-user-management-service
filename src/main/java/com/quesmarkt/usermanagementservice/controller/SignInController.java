package com.quesmarkt.usermanagementservice.controller;

import com.quesmarkt.usermanagementservice.data.request.GoogleLoginRequest;
import com.quesmarkt.usermanagementservice.data.request.SignInRequest;
import com.quesmarkt.usermanagementservice.data.response.SignInResponse;
import com.quesmarkt.usermanagementservice.service.SignInService;
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

    @PostMapping("/basic")
    public ResponseEntity<SignInResponse> signUp(@RequestBody SignInRequest request) {
        return signInService.basicSignIn(request);
    }

    @PostMapping("/google-auth")
    public ResponseEntity<SignInResponse> googleSignIn(@RequestBody GoogleLoginRequest request) {
        return signInService.googleSignIn(request);
    }
}