package dev.nano.order;

import dev.nano.clients.order.OrderRequest;

import java.util.List;

public interface OrderService {
    OrderDTO getOrder(Long id);
    List<OrderDTO> getAllOrders();
    OrderDTO createOrder(OrderRequest order);           // REST + Feign (synchronous)
    OrderDTO createOrderWithGrpc(OrderRequest order);   // gRPC + Protobuf (synchronous)
    OrderDTO createOrderSaga(OrderRequest order);       // Saga (Choreography — async)
    void confirmOrder(Long orderId);
    void cancelOrder(Long orderId);
}
