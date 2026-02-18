package dev.nano.order.query;

import dev.nano.clients.order.OrderCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

// CQRS projection - consumes OrderCreatedEvent and builds the read model
@Component
@AllArgsConstructor
@Slf4j
public class OrderViewProjection {

    private final OrderViewRepository orderViewRepository;

    @RabbitListener(queues = "${rabbitmq.queue.order-view}")
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Projection received OrderCreatedEvent for orderId: {}", event.getOrderId());

        OrderViewEntity view = OrderViewEntity.builder()
                .orderId(event.getOrderId())
                .customerId(event.getCustomerId())
                .customerName(event.getCustomerName())
                .customerEmail(event.getCustomerEmail())
                .productId(event.getProductId())
                .productName(event.getProductName())
                .productPrice(event.getProductPrice())
                .amount(event.getAmount())
                .createdAt(event.getCreatedAt())
                .build();

        orderViewRepository.save(view);
        log.info("Order view persisted for orderId: {}", event.getOrderId());
    }
}
