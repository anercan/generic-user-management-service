package com.quesmarkt.usermanagementservice.util;

import com.quesmarkt.usermanagementservice.data.entity.User;
import com.quesmarkt.usermanagementservice.data.enums.UserState;
import com.quesmarkt.usermanagementservice.data.request.SignUpRequest;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * @author anercan
 */
public class UserUtils {

    public static User createInitialUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setEmail(signUpRequest.getMail());
        boolean isPasswordSet = Objects.nonNull(signUpRequest.getPassword());
        user.setPassword(isPasswordSet ? signUpRequest.getPassword() : UUID.randomUUID().toString());
        user.setState(isPasswordSet ? UserState.ACTIVE : UserState.WAITING_FOR_SET_PASSWORD);
        user.setCreatedDate(LocalDateTime.now());
        user.setUsername(StringUtils.isEmpty(signUpRequest.getUsername()) ? StringUtils.substringBefore(signUpRequest.getMail(), "@") : signUpRequest.getUsername());
        return user;
    }
}
