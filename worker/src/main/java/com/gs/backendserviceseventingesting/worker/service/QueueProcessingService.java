package com.gs.backendserviceseventingesting.worker.service;

import com.gs.backendserviceseventingesting.worker.db.GameEventsRepository;
import com.gs.backendserviceseventingesting.worker.model.Event;
import com.gs.backendserviceseventingesting.worker.model.GameEvent;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class QueueProcessingService {

    private final GameEventsRepository eventsRepository;

    public QueueProcessingService(GameEventsRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    @RabbitListener(queues = "${spring.rabbitmq.template.queue}")
    public void processEvent(Message gameEvent) {
        System.out.println("Message from rabbitmq received");
        System.out.println("Message: " + gameEvent);
//        eventsRepository.save(convertGameEventToEvent(gameEvent));
    }

    private Event convertGameEventToEvent(GameEvent gameEvent) {
        return Event.builder()
                .id(gameEvent.getEventId())
                .player_id(gameEvent.getPlayerId())
                .event_time(gameEvent.getTimestamp())
                .event_code(gameEvent.getEventCode())
                .event_desc(gameEvent.getEventDesc())
                .build();
    }
}
