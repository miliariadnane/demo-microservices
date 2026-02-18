package dev.nano.order;

import dev.nano.amqp.RabbitMQProducer;
import dev.nano.clients.notification.NotificationRequest;
import dev.nano.clients.order.OrderCreatedEvent;
import dev.nano.clients.order.OrderRequest;
import dev.nano.clients.product.ProductClient;
import dev.nano.clients.product.ProductResponse;
import dev.nano.exceptionhandler.business.NotificationException;
import dev.nano.exceptionhandler.business.OrderException;
import dev.nano.exceptionhandler.core.ResourceNotFoundException;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

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

    @CircuitBreaker(name = "productService", fallbackMethod = "createOrderFallback")
    @Override
    public OrderDTO createOrder(OrderRequest orderRequest) {
        try {
            ProductResponse product = productClient.getProduct(orderRequest.getProductId());

            OrderEntity order = OrderEntity.builder()
                    .customerId(orderRequest.getCustomerId())
                    .productId(orderRequest.getProductId())
                    .amount(orderRequest.getAmount())
                    .createAt(LocalDateTime.now())
                    .build();

            OrderEntity savedOrder = orderRepository.save(order);
            sendOrderNotification(orderRequest);
            publishOrderCreatedEvent(savedOrder, orderRequest, product);

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

    private void publishOrderCreatedEvent(OrderEntity savedOrder, OrderRequest orderRequest, ProductResponse product) {
        try {
            OrderCreatedEvent event = OrderCreatedEvent.builder()
                    .orderId(savedOrder.getId())
                    .customerId(orderRequest.getCustomerId())
                    .customerName(orderRequest.getCustomerName())
                    .customerEmail(orderRequest.getCustomerEmail())
                    .productId(orderRequest.getProductId())
                    .productName(product.getName())
                    .productPrice(product.getPrice())
                    .amount(orderRequest.getAmount())
                    .createdAt(savedOrder.getCreateAt())
                    .build();

            rabbitMQProducer.publish(
                    "internal.exchange",
                    "internal.order.routing-key",
                    event
            );
        } catch (Exception e) {
            log.error("Failed to publish OrderCreatedEvent for orderId {}: {}", savedOrder.getId(), e.getMessage());
        }
    }

    private OrderDTO createOrderFallback(OrderRequest orderRequest, Throwable throwable) {
        log.error("Fallback triggered for createOrder: {}", throwable.getMessage());
        throw new OrderException("Product service is currently unavailable. Please try again later.");
    }
}
