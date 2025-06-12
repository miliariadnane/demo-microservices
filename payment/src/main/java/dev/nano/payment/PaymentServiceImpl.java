package dev.nano.payment;

import dev.nano.amqp.RabbitMQProducer;
import dev.nano.clients.notification.NotificationRequest;
import dev.nano.clients.order.OrderClient;
import dev.nano.clients.payment.PaymentRequest;
import dev.nano.exceptionhandler.business.NotificationException;
import dev.nano.exceptionhandler.business.PaymentException;
import dev.nano.exceptionhandler.core.ResourceNotFoundException;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static dev.nano.payment.PaymentConstant.*;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderClient orderClient;
    private final RabbitMQProducer rabbitMQProducer;

    @Override
    public PaymentDTO getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .map(paymentMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(PAYMENT_NOT_FOUND, paymentId)
                ));
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        List<PaymentEntity> payments = paymentRepository.findAll();
        if (payments.isEmpty()) {
            throw new ResourceNotFoundException(NO_PAYMENTS_FOUND);
        }
        return paymentMapper.toListDTO(payments);
    }

    @Override
    public PaymentDTO createPayment(PaymentRequest paymentRequest) {
        try {
            // Verify order exists
            orderClient.getOrder(paymentRequest.getOrderId());

            PaymentEntity payment = PaymentEntity.builder()
                    .customerId(paymentRequest.getCustomerId())
                    .orderId(paymentRequest.getOrderId())
                    .createAt(LocalDateTime.now())
                    .build();

            PaymentEntity savedPayment = paymentRepository.save(payment);
            sendPaymentNotification(paymentRequest);

            return paymentMapper.toDTO(savedPayment);
        } catch (FeignException e) {
            throw new PaymentException(String.format(PAYMENT_CREATE_ERROR, e.getMessage()));
        }
    }

    private void sendPaymentNotification(PaymentRequest payment) {
        try {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .customerId(payment.getCustomerId())
                    .customerName(payment.getCustomerName())
                    .customerEmail(payment.getCustomerEmail())
                    .sender("nanodev")
                    .message("Your payment has been processed successfully")
                    .build();

            rabbitMQProducer.publish(
                    "internal.exchange",
                    "internal.notification.routing-key",
                    notificationRequest
            );
        } catch (Exception e) {
            log.error("Failed to send payment notification: {}", e.getMessage());
            throw new NotificationException("Failed to send payment notification");
        }
    }
}
