package dev.nano.customer;

import dev.nano.clients.order.OrderRequest;
import dev.nano.clients.order.OrderResponse;
import dev.nano.clients.payment.PaymentRequest;
import dev.nano.clients.payment.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import dev.nano.swagger.BaseController;

import java.util.List;

import static dev.nano.customer.CustomerConstant.CUSTOMER_URI_REST_API;


@RestController
@RequestMapping(path = CUSTOMER_URI_REST_API)
@Tag(name = BaseController.CUSTOMER_TAG, description = BaseController.CUSTOMER_DESCRIPTION)
@AllArgsConstructor @Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @Operation(
            summary = "Get customer by ID",
            description = "Retrieve a customer's details using their unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{customerId}")
    @PreAuthorize("hasRole('app_admin') or @customerServiceImpl.isSameUser(authentication, #customerId)")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable("customerId") Long customerId) {
        log.info("Retrieving customer with ID: {}", customerId);
        return ResponseEntity.ok(customerService.getCustomer(customerId));
    }

    @Operation(
            summary = "Get all customers",
            description = "Retrieve a list of all customers in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of customers retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CustomerDTO.class))
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/list")
    @PreAuthorize("hasRole('app_admin')")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        log.info("Retrieving all customers");
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @Operation(
            summary = "Create new customer",
            description = "Register a new customer in the system with their details"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Customer created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        log.info("Creating new customer: {}", customerDTO);
        return new ResponseEntity<>(
                customerService.createCustomer(customerDTO),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Update customer",
            description = "Update an existing customer's information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Customer updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CustomerDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{customerId}")
    @PreAuthorize("hasRole('app_admin') or @customerServiceImpl.isSameUser(authentication, #customerId)")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerDTO customerDTO) {
        log.info("Updating customer with ID {}: {}", customerId, customerDTO);
        return ResponseEntity.ok(customerService.updateCustomer(customerId, customerDTO));
    }

    @Operation(
            summary = "Delete customer",
            description = "Remove a customer from the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasRole('app_admin')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        log.info("Deleting customer with ID: {}", customerId);
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Create customer order",
            description = "Place a new order for an existing customer"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Order created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid order data"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> customerOrders(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("Processing order for customer: {}", orderRequest);
        return new ResponseEntity<>(
                customerService.customerOrders(orderRequest),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Process customer payment",
            description = "Process a payment for a customer's order"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Payment processed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PaymentResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid payment data"),
            @ApiResponse(responseCode = "404", description = "Customer or order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/payment")
    public ResponseEntity<PaymentResponse> customerPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        log.info("Processing payment for customer: {}", paymentRequest);
        return new ResponseEntity<>(
                customerService.customerPayment(paymentRequest),
                HttpStatus.CREATED
        );
    }
}
