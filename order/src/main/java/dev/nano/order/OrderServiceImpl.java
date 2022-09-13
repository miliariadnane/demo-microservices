package dev.nano.order;

import dev.nano.amqp.RabbitMQProducer;
import dev.nano.clients.notification.NotificationRequest;
import dev.nano.clients.order.OrderRequest;
import dev.nano.clients.product.ProductClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static dev.nano.order.OrderConstant.ORDER_NOT_FOUND_EXCEPTION;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductClient productClient;
    private final RabbitMQProducer rabbitMQProducer;


    @Override
    public OrderDTO getOrder(Long id) {

        OrderEntity order = orderRepository.findById(id).orElseThrow(() ->
                new IllegalStateException(ORDER_NOT_FOUND_EXCEPTION));

        return orderMapper.toDTO(order);
    }

    @Override
    public List<OrderDTO> getAllOrders() {

        List<OrderEntity> allOrders = orderRepository.findAll();
        return orderMapper.toListDTO(allOrders);
    }

    @Override
    public OrderDTO createOrder(OrderRequest order) {

        // Check if product is exist
        productClient.getProduct(order.getProductId());

        // Create order
        OrderEntity orderEntity = orderRepository.save(OrderEntity.builder()
                .customerId(order.getCustomerId())
                .productId(order.getProductId())
                .amount(order.getAmount())
                .createAt(LocalDateTime.now()).build());

        // Create notificationRequest
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .customerId(order.getCustomerId())
                .customerName(order.getCustomerName())
                .customerEmail(order.getCustomerEmail())
                .sender("NanoDev")
                .message("Your order has been success.")
                .build();

        // Send notification
        rabbitMQProducer.publish("internal.exchange", "internal.notification.routing-key", notificationRequest);

        return orderMapper.toDTO(orderEntity);
    }

    @Override
    public OrderDTO updateOrder(Long id, OrderDTO order) {
        return null;
    }

    @Override
    public void deleteOrder(Long id) {

    }
}
