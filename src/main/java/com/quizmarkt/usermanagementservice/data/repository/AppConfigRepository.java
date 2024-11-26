package com.quizmarkt.usermanagementservice.data.repository;

import com.quizmarkt.usermanagementservice.data.entity.AppConfig;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author anercan
 */

@EnableScan
public interface AppConfigRepository extends CrudRepository<AppConfig, String> {

    Optional<AppConfig> findByPackageName(String packageName);
}
