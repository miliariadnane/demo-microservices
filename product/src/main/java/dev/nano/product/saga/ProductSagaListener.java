package dev.nano.product.saga;

import dev.nano.amqp.RabbitMQProducer;
import dev.nano.clients.saga.PaymentFailedEvent;
import dev.nano.clients.saga.PlaceOrderEvent;
import dev.nano.clients.saga.StockReservationFailedEvent;
import dev.nano.clients.saga.StockReservedEvent;
import dev.nano.product.ProductEntity;
import dev.nano.product.ProductRepository;
import dev.nano.product.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class ProductSagaListener {

    private final ProductService productService;
    private final ProductRepository productRepository;
    private final RabbitMQProducer rabbitMQProducer;

    @RabbitListener(queues = "${rabbitmq.queue.saga-place-order}")
    public void handlePlaceOrderEvent(PlaceOrderEvent event) {
        log.info("Saga — Received PlaceOrderEvent for orderId: {}, productId: {}, quantity: {}",
                event.getOrderId(), event.getProductId(), event.getAmount());

        try {
            productService.reserveStock(event.getProductId(), event.getAmount());

            ProductEntity product = productRepository.findById(event.getProductId()).orElseThrow();

            StockReservedEvent stockReservedEvent = StockReservedEvent.builder()
                    .orderId(event.getOrderId())
                    .customerId(event.getCustomerId())
                    .customerName(event.getCustomerName())
                    .customerEmail(event.getCustomerEmail())
                    .productId(event.getProductId())
                    .productName(product.getName())
                    .productPrice(product.getPrice())
                    .amount(event.getAmount())
                    .build();

            rabbitMQProducer.publish(
                    "internal.exchange",
                    "saga.stock-reserved.routing-key",
                    stockReservedEvent
            );

            log.info("Saga — StockReservedEvent published for orderId: {}", event.getOrderId());

        } catch (Exception e) {
            log.error("Saga — Stock reservation failed for orderId: {}: {}",
                    event.getOrderId(), e.getMessage());

            rabbitMQProducer.publish(
                    "internal.exchange",
                    "saga.stock-failed.routing-key",
                    StockReservationFailedEvent.builder()
                            .orderId(event.getOrderId())
                            .productId(event.getProductId())
                            .reason(e.getMessage())
                            .build()
            );
        }
    }

    // Compensating transaction — release stock when payment fails
    @RabbitListener(queues = "${rabbitmq.queue.saga-payment-failed-product}")
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("Saga (compensation) — Releasing stock for orderId: {}, productId: {}, quantity: {}",
                event.getOrderId(), event.getProductId(), event.getAmount());

        try {
            productService.releaseStock(event.getProductId(), event.getAmount());
        } catch (Exception e) {
            log.error("Saga (compensation) — Failed to release stock for orderId: {}: {}",
                    event.getOrderId(), e.getMessage());
        }
    }
}
