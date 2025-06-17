package dev.nano.payment;

import dev.nano.clients.order.OrderClient;
import dev.nano.clients.notification.NotificationRequest;
import dev.nano.amqp.RabbitMQProducer;
import dev.nano.clients.customer.CustomerClient;
import dev.nano.clients.customer.CustomerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import io.github.resilience4j.retry.annotation.Retry;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentProcessor {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final CustomerClient customerClient;
    private final RabbitMQProducer rabbitMQProducer;

    @Scheduled(fixedDelayString = "60000")
    @Retry(name = "orderService")
    public void processPendingPayments() {
        List<PaymentEntity> pendingPayments = paymentRepository.findAllByStatus(PaymentStatus.PENDING);
        if (pendingPayments.isEmpty()) {
            return;
        }

        pendingPayments.forEach(payment -> {
            try {
                orderClient.getOrder(payment.getOrderId());
                CustomerResponse customer = customerClient.getCustomer(payment.getCustomerId());
                payment.setStatus(PaymentStatus.COMPLETED);
                paymentRepository.save(payment);

                NotificationRequest notificationRequest = NotificationRequest.builder()
                        .customerId(customer.getId())
                        .customerName(customer.getName())
                        .customerEmail(customer.getEmail())
                        .sender("nanodev")
                        .message("Your pending payment has been processed successfully at " + LocalDateTime.now())
                        .build();

                rabbitMQProducer.publish(
                        "internal.exchange",
                        "internal.notification.routing-key",
                        notificationRequest
                );
                log.info("Successfully processed pending payment with id {}", payment.getId());
            } catch (Exception ex) {
                log.error("Failed to process pending payment with id {}: {}", payment.getId(), ex.getMessage());
            }
        });
    }
} 