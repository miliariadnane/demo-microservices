package dev.nano.order.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderViewDTO {
    private Long orderId;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private Long productId;
    private String productName;
    private double productPrice;
    private double amount;
    private LocalDateTime createdAt;
}
