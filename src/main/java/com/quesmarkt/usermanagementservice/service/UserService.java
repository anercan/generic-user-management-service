package com.quesmarkt.usermanagementservice.service;

import com.quesmarkt.usermanagementservice.data.entity.User;
import com.quesmarkt.usermanagementservice.data.repository.UserRepository;
import com.quesmarkt.usermanagementservice.data.response.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author anercan
 */

@Service
@AllArgsConstructor
public class UserService extends BaseService {

    private final UserRepository userRepository;

    public ServiceResponse<Boolean> isExistByEmail(String email) {
        try {
            return new ServiceResponse<>(userRepository.existsByEmail(email));
        } catch (Exception e) {
            return createFailResult(e.getMessage());
        }
    }

    public ServiceResponse<User> insert(User user) {
        try {
            return new ServiceResponse<>(userRepository.insert(user));
        } catch (Exception e) {
            return createFailResult("");
        }
    }


}
