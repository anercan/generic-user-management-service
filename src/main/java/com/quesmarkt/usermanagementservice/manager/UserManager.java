package com.quesmarkt.usermanagementservice.manager;

import com.quesmarkt.usermanagementservice.data.entity.User;
import com.quesmarkt.usermanagementservice.data.repository.UserRepository;
import com.quesmarkt.usermanagementservice.manager.exception.DataAccessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author anercan
 */

@Service
@AllArgsConstructor
public class UserManager extends BaseManager {

    private final UserRepository userRepository;

    public Boolean isExistByEmail(String email) {
        try {
            return userRepository.existsByEmail(email);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    public User insert(User user) {
        try {
            return userRepository.insert(user);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    public User getUserByMail(String mail) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(mail);
            return optionalUser.orElse(null);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

}
