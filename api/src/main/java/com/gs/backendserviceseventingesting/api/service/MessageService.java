package com.gs.backendserviceseventingesting.api.service;

import com.gs.backendserviceseventingesting.api.model.GameEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

public class MessageService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.exchange}")
    private String rabbitExchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String rabbitRoutingKey;

    public MessageService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(GameEvent event) {
        rabbitTemplate.convertAndSend(rabbitExchange, rabbitRoutingKey, event);
    }
}
