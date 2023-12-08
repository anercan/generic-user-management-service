package com.quesmarkt.usermanagementservice.data.repository;

import com.quesmarkt.usermanagementservice.data.entity.LoginTransaction;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

/**
 * @author anercan
 */

@EnableScan
public interface LoginTransactionRepository extends CrudRepository<LoginTransaction, String> {
}
