package dev.nano.order;

import dev.nano.amqp.RabbitMQProducer;
import dev.nano.clients.notification.NotificationRequest;
import dev.nano.clients.order.OrderRequest;
import dev.nano.clients.product.ProductClient;
import exceptionhandler.business.NotificationException;
import exceptionhandler.business.OrderException;
import exceptionhandler.core.ResourceNotFoundException;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static dev.nano.order.OrderConstant.*;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductClient productClient;
    private final RabbitMQProducer rabbitMQProducer;

    @Override
    public OrderDTO getOrder(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ORDER_NOT_FOUND, id)
                ));
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<OrderEntity> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException(NO_ORDERS_FOUND);
        }
        return orderMapper.toListDTO(orders);
    }

    @Override
    public OrderDTO createOrder(OrderRequest orderRequest) {
        try {
            // Verify product exists
            productClient.getProduct(orderRequest.getProductId());

            OrderEntity order = OrderEntity.builder()
                    .customerId(orderRequest.getCustomerId())
                    .productId(orderRequest.getProductId())
                    .amount(orderRequest.getAmount())
                    .createAt(LocalDateTime.now())
                    .build();

            OrderEntity savedOrder = orderRepository.save(order);
            sendOrderNotification(orderRequest);

            return orderMapper.toDTO(savedOrder);
        } catch (FeignException e) {
            throw new OrderException(String.format(ORDER_CREATE_ERROR, e.getMessage()));
        }
    }

    private void sendOrderNotification(OrderRequest order) {
        try {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .customerId(order.getCustomerId())
                    .customerName(order.getCustomerName())
                    .customerEmail(order.getCustomerEmail())
                    .sender("NanoDev")
                    .message("Your order has been created successfully")
                    .build();

            rabbitMQProducer.publish(
                    "internal.exchange",
                    "internal.notification.routing-key",
                    notificationRequest
            );
        } catch (Exception e) {
            log.error("Failed to send order notification: {}", e.getMessage());
            throw new NotificationException("Failed to send order notification");
        }
    }
}
