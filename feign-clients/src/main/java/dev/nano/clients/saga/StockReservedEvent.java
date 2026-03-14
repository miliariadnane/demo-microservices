package dev.nano.clients.saga;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockReservedEvent {
    private Long orderId;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private Long productId;
    private String productName;
    private double productPrice;
    private Integer amount;
}
