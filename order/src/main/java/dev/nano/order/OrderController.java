package dev.nano.order;

import dev.nano.clients.order.OrderRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

import static dev.nano.order.OrderConstant.ORDER_URI_REST_API;

@RestController
@RequestMapping(path = ORDER_URI_REST_API)
@AllArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable("orderId") Long orderId) {
        log.info("Retrieving order with id {}", orderId);
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        log.info("Retrieving all orders");
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping("/add")
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("Creating new order: {}", orderRequest);
        return new ResponseEntity<>(
                orderService.createOrder(orderRequest),
                HttpStatus.CREATED
        );
    }
}
