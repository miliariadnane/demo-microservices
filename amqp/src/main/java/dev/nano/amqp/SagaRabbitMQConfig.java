package dev.nano.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SagaRabbitMQConfig {

    @Value("${rabbitmq.exchange.internal}")
    private String internalExchange;

    @Value("${rabbitmq.queue.saga-place-order}")
    private String sagaPlaceOrderQueue;

    @Value("${rabbitmq.queue.saga-stock-reserved}")
    private String sagaStockReservedQueue;

    @Value("${rabbitmq.queue.saga-stock-failed}")
    private String sagaStockFailedQueue;

    @Value("${rabbitmq.queue.saga-payment-completed}")
    private String sagaPaymentCompletedQueue;

    @Value("${rabbitmq.queue.saga-payment-failed-order}")
    private String sagaPaymentFailedOrderQueue;

    @Value("${rabbitmq.queue.saga-payment-failed-product}")
    private String sagaPaymentFailedProductQueue;

    @Value("${rabbitmq.routing-key.saga-place-order}")
    private String sagaPlaceOrderRoutingKey;

    @Value("${rabbitmq.routing-key.saga-stock-reserved}")
    private String sagaStockReservedRoutingKey;

    @Value("${rabbitmq.routing-key.saga-stock-failed}")
    private String sagaStockFailedRoutingKey;

    @Value("${rabbitmq.routing-key.saga-payment-completed}")
    private String sagaPaymentCompletedRoutingKey;

    @Value("${rabbitmq.routing-key.saga-payment-failed}")
    private String sagaPaymentFailedRoutingKey;

    // Queues

    @Bean
    Queue sagaPlaceOrderQueue() {
        return new Queue(this.sagaPlaceOrderQueue);
    }

    @Bean
    Queue sagaStockReservedQueue() {
        return new Queue(this.sagaStockReservedQueue);
    }

    @Bean
    Queue sagaStockFailedQueue() {
        return new Queue(this.sagaStockFailedQueue);
    }

    @Bean
    Queue sagaPaymentCompletedQueue() {
        return new Queue(this.sagaPaymentCompletedQueue);
    }

    @Bean
    Queue sagaPaymentFailedOrderQueue() {
        return new Queue(this.sagaPaymentFailedOrderQueue);
    }

    @Bean
    Queue sagaPaymentFailedProductQueue() {
        return new Queue(this.sagaPaymentFailedProductQueue);
    }

    // Bindings

    @Bean
    Binding sagaPlaceOrderBinding(TopicExchange internalExchange) {
        return BindingBuilder.bind(sagaPlaceOrderQueue())
                .to(internalExchange)
                .with(this.sagaPlaceOrderRoutingKey);
    }

    @Bean
    Binding sagaStockReservedBinding(TopicExchange internalExchange) {
        return BindingBuilder.bind(sagaStockReservedQueue())
                .to(internalExchange)
                .with(this.sagaStockReservedRoutingKey);
    }

    @Bean
    Binding sagaStockFailedBinding(TopicExchange internalExchange) {
        return BindingBuilder.bind(sagaStockFailedQueue())
                .to(internalExchange)
                .with(this.sagaStockFailedRoutingKey);
    }

    @Bean
    Binding sagaPaymentCompletedBinding(TopicExchange internalExchange) {
        return BindingBuilder.bind(sagaPaymentCompletedQueue())
                .to(internalExchange)
                .with(this.sagaPaymentCompletedRoutingKey);
    }

    @Bean
    Binding sagaPaymentFailedOrderBinding(TopicExchange internalExchange) {
        return BindingBuilder.bind(sagaPaymentFailedOrderQueue())
                .to(internalExchange)
                .with(this.sagaPaymentFailedRoutingKey);
    }

    @Bean
    Binding sagaPaymentFailedProductBinding(TopicExchange internalExchange) {
        return BindingBuilder.bind(sagaPaymentFailedProductQueue())
                .to(internalExchange)
                .with(this.sagaPaymentFailedRoutingKey);
    }
}
