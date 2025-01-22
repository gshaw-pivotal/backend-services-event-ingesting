package com.gs.backendserviceseventingesting.worker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gs.backendserviceseventingesting.worker.db.GameEventsRepository;
import com.gs.backendserviceseventingesting.worker.model.Event;
import com.gs.backendserviceseventingesting.worker.model.GameEvent;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;

public class QueueProcessingService {

    @Value("${event.queue.max-attempts}")
    private Long maxRetryAttempts;

    private final GameEventsRepository eventsRepository;

    private final QueueRetryService queueRetryService;

    public QueueProcessingService(
            GameEventsRepository eventsRepository,
            QueueRetryService queueRetryService
    ) {
        this.eventsRepository = eventsRepository;
        this.queueRetryService = queueRetryService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.template.queue}")
    public void processEvent(Message event) {
        try {
            GameEvent gameEvent = new ObjectMapper().readValue(event.getBody(), GameEvent.class);
            eventsRepository.save(convertGameEventToEvent(gameEvent));
        } catch (Exception e) {
            System.out.println("Exception processing event: " + e.getMessage() );

            // Call retry service to send back onto the queue
            Long eventRetryCount = event.getMessageProperties().getRetryCount();
            if (eventRetryCount < maxRetryAttempts) {
                event.getMessageProperties().incrementRetryCount();
                queueRetryService.resendMessage(event);
            } else {
                System.out.println("Retry count exceeded");
            }
        }
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
