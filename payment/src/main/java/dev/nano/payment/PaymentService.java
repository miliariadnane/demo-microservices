package dev.nano.payment;

import dev.nano.clients.payment.PaymentRequest;

import java.util.List;

public interface PaymentService {

    PaymentDTO getPayment(Long paymentId);
    List<PaymentDTO> getAllPayments();
    PaymentDTO createPayment(PaymentRequest payment);
}
