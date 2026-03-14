package dev.nano.clients.saga;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockReservationFailedEvent {
    private Long orderId;
    private Long productId;
    private String reason;
}
