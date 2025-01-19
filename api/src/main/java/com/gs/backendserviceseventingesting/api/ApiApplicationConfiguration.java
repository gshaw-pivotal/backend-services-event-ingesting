package com.gs.backendserviceseventingesting.api;

import com.gs.backendserviceseventingesting.api.db.GameEventsRepository;
import com.gs.backendserviceseventingesting.api.service.GameEventService;
import com.gs.backendserviceseventingesting.api.service.MessageService;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiApplicationConfiguration {

    @Value("${spring.rabbitmq.host}")
    private String rabbitmqHost;

    @Value("${spring.rabbitmq.username}")
    private String rabbitmqUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitmqPassword;

    @Value("${spring.rabbitmq.template.exchange}")
    private String rabbitExchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String rabbitRoutingKey;

    @Bean
    public GameEventService gameEventService(
            MessageService messageService,
            GameEventsRepository eventsRepository) {
        return new GameEventService(messageService, eventsRepository);
    }

    @Bean
    public MessageService messageService(RabbitTemplate rabbitTemplate) {
        return new MessageService(rabbitTemplate, rabbitExchange, rabbitRoutingKey);
    }

    @Bean
    CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(rabbitmqHost);
        cachingConnectionFactory.setUsername(rabbitmqUsername);
        cachingConnectionFactory.setPassword(rabbitmqPassword);
        return cachingConnectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
