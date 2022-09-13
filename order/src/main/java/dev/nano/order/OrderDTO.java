package dev.nano.order;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long id;
    private Long customerId;
    private Long productId;
    private Integer amount;
    private LocalDateTime createAt;
}
