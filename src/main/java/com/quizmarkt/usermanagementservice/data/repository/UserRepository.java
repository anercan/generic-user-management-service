package com.quizmarkt.usermanagementservice.data.repository;

import com.quizmarkt.usermanagementservice.data.entity.User;
import com.quizmarkt.usermanagementservice.data.enums.PremiumType;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByEmailAndAppId(String email, Integer appId);

    @SuppressWarnings("NullableProblems")
    @Override
    Optional<User> findById(String id);

    List<User> findAllByPremiumInfo_PremiumType(PremiumType premiumType);

}