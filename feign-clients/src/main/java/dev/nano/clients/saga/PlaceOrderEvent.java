package dev.nano.clients.saga;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderEvent {
    private Long orderId;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private Long productId;
    private Integer amount;
}
