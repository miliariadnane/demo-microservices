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
import dev.nano.grpc.product.GetProductResponse;
import dev.nano.order.grpc.ProductGrpcClient;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    private final ProductClient productClient;         // REST + Feign  (HTTP/1.1 + JSON)
    private final ProductGrpcClient productGrpcClient; // gRPC + Protobuf (HTTP/2 + binary)
    private final RabbitMQProducer rabbitMQProducer;

    @Override
    public OrderDTO getOrder(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ORDER_NOT_FOUND, id)));
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<OrderEntity> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException(NO_ORDERS_FOUND);
        }
        return orderMapper.toListDTO(orders);
    }

    // REST + Feign
    @CircuitBreaker(name = "productService", fallbackMethod = "createOrderFallback")
    @Override
    public OrderDTO createOrder(OrderRequest orderRequest) {
        long start = System.nanoTime();
        try {
            ProductResponse product = productClient.getProduct(orderRequest.productId());
            OrderEntity savedOrder = saveOrder(orderRequest);
            sendOrderNotification(orderRequest);
            publishOrderCreatedEvent(savedOrder, orderRequest, product.name(), product.price());
            return orderMapper.toDTO(savedOrder);
        } catch (FeignException e) {
            throw new OrderException(String.format(ORDER_CREATE_ERROR, e.getMessage()));
        } finally {
            log.info("[REST + Feign] createOrder executed in {} ms", (System.nanoTime() - start) / 1_000_000);
        }
    }

    // gRPC + Protobuf
    @CircuitBreaker(name = "productService", fallbackMethod = "createOrderWithGrpcFallback")
    @Override
    public OrderDTO createOrderWithGrpc(OrderRequest orderRequest) {
        long start = System.nanoTime();
        try {
            GetProductResponse product = productGrpcClient.getProduct(orderRequest.productId());
            OrderEntity savedOrder = saveOrder(orderRequest);
            sendOrderNotification(orderRequest);
            publishOrderCreatedEvent(savedOrder, orderRequest, product.getName(), product.getPrice());
            return orderMapper.toDTO(savedOrder);
        } finally {
            log.info("[gRPC + Protobuf] createOrderWithGrpc executed in {} ms", (System.nanoTime() - start) / 1_000_000);
        }
    }

    private OrderEntity saveOrder(OrderRequest orderRequest) {
        return orderRepository.save(
                OrderEntity.builder()
                        .customerId(orderRequest.customerId())
                        .productId(orderRequest.productId())
                        .amount(orderRequest.amount())
                        .createAt(LocalDateTime.now())
                        .build()
        );
    }

    private void sendOrderNotification(OrderRequest order) {
        try {
            rabbitMQProducer.publish(
                    "internal.exchange",
                    "internal.notification.routing-key",
                    NotificationRequest.builder()
                            .customerId(order.customerId())
                            .customerName(order.customerName())
                            .customerEmail(order.customerEmail())
                            .sender("NanoDev")
                            .message("Your order has been created successfully")
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to send order notification: {}", e.getMessage());
            throw new NotificationException("Failed to send order notification");
        }
    }

    private void publishOrderCreatedEvent(OrderEntity savedOrder, OrderRequest orderRequest,
                                          String productName, double productPrice) {
        try {
            rabbitMQProducer.publish(
                    "internal.exchange",
                    "internal.order.routing-key",
                    OrderCreatedEvent.builder()
                            .orderId(savedOrder.getId())
                            .customerId(orderRequest.customerId())
                            .customerName(orderRequest.customerName())
                            .customerEmail(orderRequest.customerEmail())
                            .productId(orderRequest.productId())
                            .productName(productName)
                            .productPrice(productPrice)
                            .amount(orderRequest.amount())
                            .createdAt(savedOrder.getCreateAt())
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to publish OrderCreatedEvent for orderId {}: {}", savedOrder.getId(), e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    private OrderDTO createOrderFallback(OrderRequest orderRequest, Throwable throwable) {
        log.error("Circuit breaker fallback — createOrder (REST): {}", throwable.getMessage());
        throw new OrderException("Product service is currently unavailable. Please try again later.");
    }

    @SuppressWarnings("unused")
    private OrderDTO createOrderWithGrpcFallback(OrderRequest orderRequest, Throwable throwable) {
        log.error("Circuit breaker fallback — createOrderWithGrpc: {}", throwable.getMessage());
        throw new OrderException("Product service is currently unavailable. Please try again later.");
    }
}
