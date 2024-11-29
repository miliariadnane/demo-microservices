package dev.nano.customer;

import dev.nano.clients.order.OrderRequest;
import dev.nano.clients.order.OrderResponse;
import dev.nano.clients.payment.PaymentRequest;
import dev.nano.clients.payment.PaymentResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static dev.nano.customer.CustomerConstant.CUSTOMER_URI_REST_API;


@RestController
@RequestMapping(path = CUSTOMER_URI_REST_API)
@AllArgsConstructor @Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping(path = "/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable("customerId") Long customerId) {
        log.info("Retrieving customer with ID: {}", customerId);
        return ResponseEntity.ok(customerService.getCustomer(customerId));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        log.info("Retrieving all customers");
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PostMapping("/add")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        log.info("Creating new customer: {}", customerDTO);
        return new ResponseEntity<>(
                customerService.createCustomer(customerDTO),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerDTO customerDTO) {
        log.info("Updating customer with ID {}: {}", customerId, customerDTO);
        return ResponseEntity.ok(customerService.updateCustomer(customerId, customerDTO));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        log.info("Deleting customer with ID: {}", customerId);
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> customerOrders(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("Processing order for customer: {}", orderRequest);
        return new ResponseEntity<>(
                customerService.customerOrders(orderRequest),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/payment")
    public ResponseEntity<PaymentResponse> customerPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        log.info("Processing payment for customer: {}", paymentRequest);
        return new ResponseEntity<>(
                customerService.customerPayment(paymentRequest),
                HttpStatus.CREATED
        );
    }
}
