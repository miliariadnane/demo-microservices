package dev.nano.order.query;

import java.util.List;

public interface OrderQueryService {
    OrderViewDTO getOrderView(Long orderId);
    List<OrderViewDTO> getAllOrderViews();
    List<OrderViewDTO> getOrderViewsByCustomer(Long customerId);
}
