package com.gs.backendserviceseventingesting.worker;

import com.gs.backendserviceseventingesting.worker.db.GameEventsRepository;
import com.gs.backendserviceseventingesting.worker.service.QueueProcessingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkerApplicationConfiguration {

    @Bean
    public QueueProcessingService queueProcessingService(GameEventsRepository eventsRepository) {
        return new QueueProcessingService(eventsRepository);
    }
}
