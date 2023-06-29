package com.quesmarkt.usermanagementservice.controller;

import com.quesmarkt.usermanagementservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author anercan
 */

@Controller
@AllArgsConstructor
@RequestMapping("/test")
public class test {

    private final UserService userService;

    @GetMapping
    public void getUser() {
        userService.test();
    }

}
