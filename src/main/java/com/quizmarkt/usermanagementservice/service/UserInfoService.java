package com.quizmarkt.usermanagementservice.service;

import com.quizmarkt.usermanagementservice.data.entity.User;
import com.quizmarkt.usermanagementservice.data.request.UserFilterRequest;
import com.quizmarkt.usermanagementservice.data.response.UserInfo;
import com.quizmarkt.usermanagementservice.manager.UserManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author anercan
 */

@Service
@AllArgsConstructor
@Slf4j
public class UserInfoService {

    private UserManager userManager;

    public ResponseEntity<UserInfo> getUserInfo(String userId) {
        try {
            Optional<User> userById = userManager.getUserById(userId);
            if (userById.isPresent()) {
                UserInfo userInfo = new UserInfo();
                User user = userById.get();
                userInfo.setAvatarUrl(user.getAvatarUrl());
                return ResponseEntity.ok(userInfo);
            }
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "User Couldn't found with id:" + userId)).build();
        } catch (Exception e) {
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage())).build();
        }
    }

    public ResponseEntity<List<User>> getUsersByFilter(UserFilterRequest userFilterDto) {
        List<User> filteredUsers = userManager.getAllByFilter(userFilterDto);
        List<User> sortedAndFilteredUsers = filteredUsers.stream().sorted(Comparator.comparing(User::getCreatedDate).reversed()).collect(Collectors.toList());
        return ResponseEntity.ok(sortedAndFilteredUsers);
    }
}
