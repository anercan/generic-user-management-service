package com.quesmarkt.usermanagementservice.data.repository;

import com.quesmarkt.usermanagementservice.data.entity.User;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface UserRepository extends CrudRepository<User, String> {

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndAppId(String email,Integer appId);

    @Override
    Optional<User> findById(String id);
}