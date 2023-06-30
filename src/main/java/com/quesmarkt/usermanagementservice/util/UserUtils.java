package com.quesmarkt.usermanagementservice.util;

import com.quesmarkt.usermanagementservice.data.entity.User;
import com.quesmarkt.usermanagementservice.data.request.SignUpRequest;
import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * @author anercan
 */
public class UserUtils {

    public static User createInitialUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(UUID.randomUUID().toString());
        user.setActive(false);
        user.setAvatarUrl("");
        user.setCreatedDate(ZonedDateTime.now());
        user.setUsername(StringUtils.substringBefore(signUpRequest.getEmail(), "@"));
        return user;
    }
}
