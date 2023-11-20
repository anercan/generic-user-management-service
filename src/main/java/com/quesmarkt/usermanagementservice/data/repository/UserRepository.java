package com.quesmarkt.usermanagementservice.data.repository;

import com.quesmarkt.usermanagementservice.data.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByEmail(String email);

}