package com.quesmarkt.usermanagementservice.manager;

import com.quesmarkt.usermanagementservice.data.entity.PremiumInfo;
import com.quesmarkt.usermanagementservice.data.entity.User;
import com.quesmarkt.usermanagementservice.data.repository.UserRepository;
import com.quesmarkt.usermanagementservice.data.request.PremiumInfoRequest;
import com.quesmarkt.usermanagementservice.manager.exception.AppException;
import com.quesmarkt.usermanagementservice.manager.exception.DataAccessException;
import com.quesmarkt.usermanagementservice.manager.exception.UserNotFoundException;
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

    public Optional<User> insert(User user) {
        try {
            return Optional.of(userRepository.save(user));
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    public Optional<User> getUserByMail(String mail, Integer appId) {
        try {
            return userRepository.findByEmailAndAppId(mail, appId);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    public User setUserPremiumInfo(PremiumInfoRequest request) {
        Optional<User> userOpt = userRepository.findById(request.getUserId());
        if (userOpt.isEmpty()) {
            logger.warn("setUserPremiumInfo - User couldn't found! userID:{}", request.getUserId());
            throw new UserNotFoundException("User couldn't found!");
        }
        try {
            User user = userOpt.get();
            PremiumInfo premiumInfo = new PremiumInfo();
            premiumInfo.setPremiumType(request.getPremiumType());
            premiumInfo.setExpireDate(request.getExpireDate());
            user.setPremiumInfo(premiumInfo);
            userRepository.save(user);
            logger.info("User premium info updated! userID:{}", request.getUserId());
            return user;
        } catch (Exception e) {
            logger.error("User premium info got exception! userID:{}", request.getUserId());
            throw new AppException(e);
        }
    }
}
