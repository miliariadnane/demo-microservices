package dev.nano.payment;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class PaymentDbSeeder {

    @Bean
    CommandLineRunner commandLineRunner(PaymentRepository paymentRepository) {
        return args -> {
            PaymentEntity payment = PaymentEntity.builder()
                    .customerId(1L)
                    .orderId(1L)
                    .createAt(LocalDateTime.now())
                    .build();
            paymentRepository.save(payment);
        };
    }
}
