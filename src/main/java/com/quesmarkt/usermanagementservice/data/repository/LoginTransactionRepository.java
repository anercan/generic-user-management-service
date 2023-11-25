package com.quesmarkt.usermanagementservice.data.repository;

import com.quesmarkt.usermanagementservice.data.entity.LoginTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author anercan
 */

@Repository
public interface LoginTransactionRepository extends MongoRepository<LoginTransaction, String> {
}
