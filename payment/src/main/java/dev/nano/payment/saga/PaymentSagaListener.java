package dev.nano.payment.saga;

import dev.nano.amqp.RabbitMQProducer;
import dev.nano.clients.saga.PaymentCompletedEvent;
import dev.nano.clients.saga.PaymentFailedEvent;
import dev.nano.clients.saga.StockReservedEvent;
import dev.nano.payment.PaymentEntity;
import dev.nano.payment.PaymentRepository;
import dev.nano.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
@Slf4j
public class PaymentSagaListener {

    private final PaymentRepository paymentRepository;
    private final RabbitMQProducer rabbitMQProducer;

    @RabbitListener(queues = "${rabbitmq.queue.saga-stock-reserved}")
    public void handleStockReserved(StockReservedEvent event) {
        log.info("Saga — Processing payment for orderId: {}", event.getOrderId());

        try {
            PaymentEntity payment = PaymentEntity.builder()
                    .customerId(event.getCustomerId())
                    .orderId(event.getOrderId())
                    .status(PaymentStatus.COMPLETED)
                    .createAt(LocalDateTime.now())
                    .build();

            PaymentEntity savedPayment = paymentRepository.save(payment);

            rabbitMQProducer.publish(
                    "internal.exchange",
                    "saga.payment-completed.routing-key",
                    PaymentCompletedEvent.builder()
                            .paymentId(savedPayment.getId())
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

            log.info("Saga — PaymentCompletedEvent published for orderId: {}, paymentId: {}",
                    event.getOrderId(), savedPayment.getId());

        } catch (Exception e) {
            log.error("Saga — Payment failed for orderId: {}: {}", event.getOrderId(), e.getMessage());

            paymentRepository.save(PaymentEntity.builder()
                    .customerId(event.getCustomerId())
                    .orderId(event.getOrderId())
                    .status(PaymentStatus.FAILED)
                    .createAt(LocalDateTime.now())
                    .build());

            rabbitMQProducer.publish(
                    "internal.exchange",
                    "saga.payment-failed.routing-key",
                    PaymentFailedEvent.builder()
                            .orderId(event.getOrderId())
                            .customerId(event.getCustomerId())
                            .productId(event.getProductId())
                            .amount(event.getAmount())
                            .reason(e.getMessage())
                            .build()
            );
        }
    }
}
