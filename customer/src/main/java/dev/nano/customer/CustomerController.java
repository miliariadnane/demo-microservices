package dev.nano.customer;

import dev.nano.clients.order.OrderRequest;
import dev.nano.clients.order.OrderResponse;
import dev.nano.clients.payment.PaymentRequest;
import dev.nano.clients.payment.PaymentResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dev.nano.customer.CustomerConstant.CUSTOMER_URI_REST_API;


@RestController
@RequestMapping(path = CUSTOMER_URI_REST_API)
@AllArgsConstructor @Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping(
            path = "/{customerId}",
            produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable("customerId") Long customerId)
            throws CustomerNotFoundException {

        log.info("Retrieving customer with id {}", customerId);
        return new ResponseEntity<>(
            customerService.getCustomer(customerId),
            HttpStatus.OK
        );
    }

    @PostMapping(
            path = "/add",
            consumes={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<CustomerDTO> createNewCustomer(@RequestBody CustomerDTO customerDTO) {
        log.info("Add new customer {}", customerDTO);
        return new ResponseEntity<>(
            customerService.createCustomer(customerDTO),
            HttpStatus.CREATED
        );
    }

    @PostMapping(
            path = "/orders",
            produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<OrderResponse> customerOrders(@RequestBody OrderRequest orderRequest) {

        log.info("Customer orders {}", orderRequest);
        return new ResponseEntity<>(
            customerService.customerOrders(orderRequest),
            HttpStatus.CREATED
        );
    }

    @PostMapping(
            path = "/payment",
            produces={MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<PaymentResponse> customerPayment(@RequestBody PaymentRequest paymentRequest) {

        log.info("Customer payment {}", paymentRequest);
        return new ResponseEntity<>(
                customerService.customerPayment(paymentRequest),
                HttpStatus.CREATED
        );
    }
}
