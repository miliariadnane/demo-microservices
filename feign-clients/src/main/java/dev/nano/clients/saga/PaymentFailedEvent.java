package dev.nano.clients.saga;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentFailedEvent {
    private Long orderId;
    private Long customerId;
    private Long productId;
    private Integer amount;
    private String reason;
}
