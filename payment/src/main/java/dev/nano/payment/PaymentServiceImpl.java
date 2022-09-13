package dev.nano.payment;

import dev.nano.amqp.RabbitMQProducer;
import dev.nano.clients.notification.NotificationRequest;
import dev.nano.clients.order.OrderClient;
import dev.nano.clients.payment.PaymentRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static dev.nano.payment.PaymentConstant.PAYMENT_NOT_FOUND_EXCEPTION;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderClient orderClient;
    private final RabbitMQProducer rabbitMQProducer;

    @Override
    public PaymentDTO getPayment(Long paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId).orElseThrow(() ->
                new IllegalStateException(PAYMENT_NOT_FOUND_EXCEPTION));

        return paymentMapper.toDTO(payment);
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        List<PaymentEntity> paymentList = paymentRepository.findAll();
        return paymentMapper.toListDTO(paymentList);
    }

    @Override
    public PaymentDTO createPayment(PaymentRequest payment) {

        // find order
        orderClient.getOrder(payment.getOrderId());

        // add new payment
        PaymentEntity paymentEntity = paymentRepository.save(PaymentEntity.builder()
                .customerId(payment.getCustomerId())
                .orderId(payment.getOrderId())
                .createAt(LocalDateTime.now()).build());

        // create notification request
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .customerId(payment.getCustomerId())
                .customerName(payment.getCustomerName())
                .customerEmail(payment.getCustomerEmail())
                .sender("nanodev")
                .message("Your payment passed successfully. Thank you")
                .build();

        // publishing notification to rabbitmq
        rabbitMQProducer.publish("internal.exchange", "internal.notification.routing-key", notificationRequest);

        return paymentMapper.toDTO(paymentEntity);
    }
}
