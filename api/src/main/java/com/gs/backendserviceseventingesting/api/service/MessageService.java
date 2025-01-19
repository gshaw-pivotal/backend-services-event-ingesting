package com.gs.backendserviceseventingesting.api.service;

import com.gs.backendserviceseventingesting.api.model.GameEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class MessageService {

    private final RabbitTemplate rabbitTemplate;

    private final String rabbitExchange;

    private final String rabbitRoutingKey;

    public MessageService(
            RabbitTemplate rabbitTemplate,
            String rabbitExchange,
            String rabbitRoutingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitExchange = rabbitExchange;
        this.rabbitRoutingKey = rabbitRoutingKey;
    }

    public void sendMessage(GameEvent event) {
        rabbitTemplate.convertAndSend(rabbitExchange, rabbitRoutingKey, event);
    }
}
