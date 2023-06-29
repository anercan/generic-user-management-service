package com.quesmarkt.usermanagementservice.controller;

import com.quesmarkt.usermanagementservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author anercan
 */

@Controller
@AllArgsConstructor
@RequestMapping("/sign-in")
public class SignInController extends BaseController {

    private final UserService userService;

   /* @GetMapping
    public ResponseEntity<SignInResponse> signIn(SignInRequest request) {
        return createResponseEntity(userService.signIn());
    }*/

}