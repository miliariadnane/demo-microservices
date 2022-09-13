package dev.nano.order;

import dev.nano.clients.order.OrderRequest;

import java.util.List;

public interface OrderService {
    OrderDTO getOrder(Long id);
    List<OrderDTO> getAllOrders();
    OrderDTO createOrder(OrderRequest order);
    OrderDTO updateOrder(Long id, OrderDTO order);
    void deleteOrder(Long id);
}
