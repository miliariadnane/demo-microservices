package dev.nano.order.query;

import dev.nano.swagger.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/orders/views")
@Tag(name = BaseController.ORDER_TAG, description = "CQRS Query Side - order views")
@AllArgsConstructor
@Slf4j
public class OrderQueryController {

    private final OrderQueryService orderQueryService;

    @Operation(summary = "[CQRS Query] Get enriched order view by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order view found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderViewDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Order view not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('app_admin')")
    public ResponseEntity<OrderViewDTO> getOrderView(@PathVariable("orderId") Long orderId) {
        log.info("Retrieving order view for orderId: {}", orderId);
        return ResponseEntity.ok(orderQueryService.getOrderView(orderId));
    }

    @Operation(summary = "[CQRS Query] Get all enriched order views")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Order views retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrderViewDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/list")
    @PreAuthorize("hasRole('app_admin')")
    public ResponseEntity<List<OrderViewDTO>> getAllOrderViews() {
        log.info("Retrieving all order views");
        return ResponseEntity.ok(orderQueryService.getAllOrderViews());
    }

    @Operation(summary = "[CQRS Query] Get all order views by customer")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer order views retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OrderViewDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "404", description = "No orders found for this customer"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('app_admin')")
    public ResponseEntity<List<OrderViewDTO>> getOrderViewsByCustomer(@PathVariable("customerId") Long customerId) {
        log.info("Retrieving order views for customerId: {}", customerId);
        return ResponseEntity.ok(orderQueryService.getOrderViewsByCustomer(customerId));
    }
}
