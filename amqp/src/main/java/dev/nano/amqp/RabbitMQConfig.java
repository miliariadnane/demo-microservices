package dev.nano.amqp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private ConnectionFactory connectionFactory;
    @Value("${rabbitmq.exchange.internal}")
    private String internalExchange;
    @Value("${rabbitmq.queue.notification}")
    private String notificationQueue;
    @Value("${rabbitmq.routing-key.internal-notification}")
    private String internalNotificationRoutingKey;

    public RabbitMQConfig(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(this.notificationQueue);
    }

    @Bean
    public TopicExchange internalExchange() {
        return new TopicExchange(this.internalExchange);
    }

    // bind queue to exchange with routing key
    @Bean
    public Binding binding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(internalExchange())
                .with(this.internalNotificationRoutingKey);
    }

    // Configure amqp template that allows to send messages
    @Bean
    public AmqpTemplate amqpTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonConverter());
        return rabbitTemplate;
    }

    //Build the rabbit listener container & connect to RabbitMQ broker to listener message
    // that allows to consume messages from queues (listener)
    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonConverter());
        return factory;
    }

    // Jackson2JsonMessageConverter is used to convert messages to JSON format
    @Bean
    public MessageConverter jacksonConverter() {
        MessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        return jackson2JsonMessageConverter;
    }
}
