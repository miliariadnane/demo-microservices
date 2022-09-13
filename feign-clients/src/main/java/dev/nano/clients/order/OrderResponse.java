package dev.nano.clients.order;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderResponse {
    private Long id;
    private Long customerId;
    private Long productId;
    private Integer amount;
    private LocalDateTime createAt;
}
