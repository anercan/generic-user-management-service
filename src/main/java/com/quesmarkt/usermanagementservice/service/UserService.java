package com.quesmarkt.usermanagementservice.service;

import com.quesmarkt.usermanagementservice.data.entity.User;
import com.quesmarkt.usermanagementservice.data.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author anercan
 */

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void test() {
        User user = new User();
        user.setName("anil");
        userRepository.insert(user);
    }

}
