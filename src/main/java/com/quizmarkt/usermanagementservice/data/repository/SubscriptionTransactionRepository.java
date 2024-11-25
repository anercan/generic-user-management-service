package com.quizmarkt.usermanagementservice.data.repository;

import com.quizmarkt.usermanagementservice.data.entity.SubscriptionTransaction;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

/**
 * @author anercan
 */

@EnableScan
public interface SubscriptionTransactionRepository extends CrudRepository<SubscriptionTransaction, String> {
}
