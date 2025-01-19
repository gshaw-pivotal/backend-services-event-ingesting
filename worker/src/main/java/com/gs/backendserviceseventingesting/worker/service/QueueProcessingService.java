package com.gs.backendserviceseventingesting.worker.service;

import com.gs.backendserviceseventingesting.worker.db.GameEventsRepository;

public class QueueProcessingService {

    private final GameEventsRepository eventsRepository;

    public QueueProcessingService(GameEventsRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }
}
