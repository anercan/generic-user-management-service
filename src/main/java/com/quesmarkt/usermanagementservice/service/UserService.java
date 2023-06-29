package com.quesmarkt.usermanagementservice.service;

import com.quesmarkt.usermanagementservice.data.repository.UserRepository;
import com.quesmarkt.usermanagementservice.data.response.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author anercan
 */

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ServiceResponse<Boolean> isExistByEmail(String email) {
        return new ServiceResponse<>(userRepository.existsByEmail(email));
    }


}
