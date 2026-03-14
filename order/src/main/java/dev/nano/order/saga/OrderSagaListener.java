package dev.nano.order.saga;

import dev.nano.amqp.RabbitMQProducer;
import dev.nano.clients.notification.NotificationRequest;
import dev.nano.clients.order.OrderCreatedEvent;
import dev.nano.clients.saga.PaymentCompletedEvent;
import dev.nano.clients.saga.PaymentFailedEvent;
import dev.nano.clients.saga.StockReservationFailedEvent;
import dev.nano.order.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class OrderSagaListener {

    private final OrderService orderService;
    private final RabbitMQProducer rabbitMQProducer;

    @RabbitListener(queues = "${rabbitmq.queue.saga-stock-failed}")
    public void handleStockReservationFailed(StockReservationFailedEvent event) {
        log.info("Saga — Stock reservation failed for orderId: {}, reason: {}",
                event.getOrderId(), event.getReason());

        orderService.cancelOrder(event.getOrderId());
    }

    @RabbitListener(queues = "${rabbitmq.queue.saga-payment-completed}")
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("Saga — Payment completed for orderId: {}, paymentId: {}",
                event.getOrderId(), event.getPaymentId());

        orderService.confirmOrder(event.getOrderId());
        publishOrderViewEvent(event);
        sendOrderConfirmationNotification(event);

        log.info("Saga completed — Order {} confirmed", event.getOrderId());
    }

    @RabbitListener(queues = "${rabbitmq.queue.saga-payment-failed-order}")
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("Saga — Payment failed for orderId: {}, reason: {}",
                event.getOrderId(), event.getReason());

        orderService.cancelOrder(event.getOrderId());
    }

    private void publishOrderViewEvent(PaymentCompletedEvent event) {
        try {
            rabbitMQProducer.publish(
                    "internal.exchange",
                    "internal.order.routing-key",
                    OrderCreatedEvent.builder()
                            .orderId(event.getOrderId())
                            .customerId(event.getCustomerId())
                            .customerName(event.getCustomerName())
                            .customerEmail(event.getCustomerEmail())
                            .productId(event.getProductId())
                            .productName(event.getProductName())
                            .productPrice(event.getProductPrice())
                            .amount(event.getAmount())
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to publish CQRS view event for orderId {}: {}", event.getOrderId(), e.getMessage());
        }
    }

    private void sendOrderConfirmationNotification(PaymentCompletedEvent event) {
        try {
            rabbitMQProducer.publish(
                    "internal.exchange",
                    "internal.notification.routing-key",
                    NotificationRequest.builder()
                            .customerId(event.getCustomerId())
                            .customerName(event.getCustomerName())
                            .customerEmail(event.getCustomerEmail())
                            .sender("NanoDev")
                            .message("Your order #" + event.getOrderId() + " has been confirmed and payment received")
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to send order confirmation notification for orderId {}: {}", event.getOrderId(), e.getMessage());
        }
    }
}
