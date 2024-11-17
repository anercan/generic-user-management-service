package com.quesmarkt.usermanagementservice.data.repository;

import com.quesmarkt.usermanagementservice.data.entity.SubscriptionTransaction;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

/**
 * @author anercan
 */

@EnableScan
public interface SubscriptionTransactionRepository extends CrudRepository<SubscriptionTransaction, String> {
}
