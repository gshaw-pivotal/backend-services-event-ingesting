package com.gs.backendserviceseventingesting.worker.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class QueueRetryService {
    private final RabbitTemplate rabbitTemplate;

    private final String rabbitExchange;

    private final String rabbitRoutingKey;

    public QueueRetryService(
            RabbitTemplate rabbitTemplate,
            String rabbitExchange,
            String rabbitRoutingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitExchange = rabbitExchange;
        this.rabbitRoutingKey = rabbitRoutingKey;
    }

    public void resendMessage(Message event) {
        rabbitTemplate.send(rabbitExchange, rabbitRoutingKey, event);
    }
}
