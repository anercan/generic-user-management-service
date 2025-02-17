package com.quizmarkt.usermanagementservice.manager;

import com.quizmarkt.usermanagementservice.data.entity.AppConfig;
import com.quizmarkt.usermanagementservice.data.repository.AppConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * @author anercan
 */

@Component
@AllArgsConstructor
@Slf4j
public class AppConfigManager {

    private final AppConfigRepository appConfigRepository;
    private List<AppConfig> inMemoryAppList;

    @PostConstruct
    public void setInMemoryAppList() {
        try {
            Iterable<AppConfig> allApps = appConfigRepository.findAll();
            log.info("inMemoryAppList initiliazed.");
            inMemoryAppList = StreamSupport.stream(allApps.spliterator(), false)
                    .toList();
        } catch (Exception e) {
            log.error("setInMemoryAppList got exception", e);
            throw new RuntimeException("Critical error during startup:inMemoryAppList. ", e);
        }
    }

    public List<AppConfig> getInMemoryAppList() {
        return inMemoryAppList;
    }

    public AppConfig getInMemoryAppWithId(int appId) {
        return inMemoryAppList.stream().filter(appConfig -> appConfig.getAppId().equals(appId)).findFirst().orElse(null);
    }

}
