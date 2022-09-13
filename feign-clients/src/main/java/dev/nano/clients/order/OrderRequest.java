package dev.nano.clients.order;

import lombok.Data;

@Data
public class OrderRequest {
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private Long productId;
    private Integer amount;
}
