package com.gs.backendserviceseventingesting.worker;

import com.gs.backendserviceseventingesting.worker.db.GameEventsRepository;
import com.gs.backendserviceseventingesting.worker.service.QueueProcessingService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkerApplicationConfiguration {

    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;

    @Value("${spring.rabbitmq.username}")
    private String rabbitUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitPassword;

    @Value("${spring.rabbitmq.template.queue}")
    private String rabbitQueue;

    @Value("${spring.rabbitmq.template.exchange}")
    private String rabbitExchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String rabbitRoutingKey;

    @Bean
    public QueueProcessingService queueProcessingService(GameEventsRepository eventsRepository) {
        return new QueueProcessingService(eventsRepository);
    }

    @Bean
    Queue eventQueue() {
        return new Queue(rabbitQueue, true);
    }

    @Bean
    Exchange eventExchange() {
        return ExchangeBuilder.directExchange(rabbitExchange).durable(true).build();
    }

    @Bean
    Binding binding() {
        return BindingBuilder
                .bind(eventQueue())
                .to(eventExchange())
                .with(rabbitRoutingKey)
                .noargs();
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(rabbitHost);
        cachingConnectionFactory.setUsername(rabbitUsername);
        cachingConnectionFactory.setPassword(rabbitPassword);
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
