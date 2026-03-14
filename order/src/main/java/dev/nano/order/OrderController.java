package dev.nano.order;

import dev.nano.clients.order.OrderRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import dev.nano.swagger.BaseController;

import java.util.List;

import static dev.nano.order.OrderConstant.ORDER_URI_REST_API;

@RestController
@RequestMapping(path = ORDER_URI_REST_API)
@Tag(name = BaseController.ORDER_TAG, description = BaseController.ORDER_DESCRIPTION)
@AllArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Get order by ID",
            description = "Retrieve detailed information about a specific order using its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('app_admin')")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable("orderId") Long orderId) {
        log.info("Retrieving order with id {}", orderId);
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @Operation(
            summary = "Get all orders",
            description = "Retrieve a list of all orders with their details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Orders retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrderDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/list")
    @PreAuthorize("hasRole('app_admin')")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        log.info("Retrieving all orders");
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @Operation(
            summary = "Create order via REST + Feign (synchronous)",
            description = "Places a new order using REST. Saved as CONFIRMED immediately."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Order created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid order data"),
            @ApiResponse(responseCode = "404", description = "Product or customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("[REST] Creating order for productId={}", orderRequest.productId());
        return new ResponseEntity<>(orderService.createOrder(orderRequest), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Create order via gRPC + Protobuf (synchronous)",
            description = "Places a new order using gRPC. Saved as CONFIRMED immediately."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Order created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid order data"),
            @ApiResponse(responseCode = "404", description = "Product or customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/grpc")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDTO> createOrderWithGrpc(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("[gRPC] Creating order for productId={}", orderRequest.productId());
        return new ResponseEntity<>(orderService.createOrderWithGrpc(orderRequest), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Create order via Saga (Choreography — async)",
            description = "Saves order as PENDING and starts the saga: Product → Payment → Order confirm."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "Order accepted — saga started (status: PENDING)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid order data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/saga")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderDTO> createOrderSaga(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("[SAGA] Creating order for productId={}", orderRequest.productId());
        return new ResponseEntity<>(orderService.createOrderSaga(orderRequest), HttpStatus.ACCEPTED);
    }
}
