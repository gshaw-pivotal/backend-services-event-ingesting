package com.gs.backendserviceseventingesting.api;

import com.gs.backendserviceseventingesting.api.service.GameEventService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiApplicationConfiguration {

    @Bean
    public GameEventService gameEventService() {
        return new GameEventService();
    }
}
