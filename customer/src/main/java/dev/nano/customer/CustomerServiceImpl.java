package dev.nano.customer;

import dev.nano.amqp.RabbitMQProducer;
import dev.nano.clients.notification.NotificationRequest;
import dev.nano.clients.order.OrderClient;
import dev.nano.clients.order.OrderRequest;
import dev.nano.clients.order.OrderResponse;
import dev.nano.clients.payment.PaymentClient;
import dev.nano.clients.payment.PaymentRequest;
import dev.nano.clients.payment.PaymentResponse;
import dev.nano.clients.product.ProductClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static dev.nano.customer.CustomerConstant.CUSTOMER_NOT_FOUND_EXCEPTION;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final OrderClient orderClient;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;
    private final RabbitMQProducer rabbitMQProducer;

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {

        CustomerEntity customer = customerRepository.findById(id).orElse(null);

        if (customer == null) {
            log.error("Customer not found {}", id);
            throw new CustomerNotFoundException(CUSTOMER_NOT_FOUND_EXCEPTION);
        }

        return customerMapper.toDTO(customer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return null;
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {

        // save customer
        CustomerEntity customer = customerRepository.save(customerMapper.toEntity(customerDTO));

        // send notification to notification microservice
        NotificationRequest notificationRequest = NotificationRequest.builder()
                .customerId(customer.getId())
                .customerName(customer.getName())
                .customerEmail(customer.getEmail())
                .sender("nanodev")
                .message("Hello " + customer.getName() + ". Welcome to nanodev demo project on microservices")
                .build();

        rabbitMQProducer.publish(
                "internal.exchange",
                "internal.notification.routing-key",
                notificationRequest
        );

        log.info("New customer created successfully {}", customer);

        return customerMapper.toDTO(customer);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customer) {
        return null;
    }

    @Override
    public void deleteCustomer(Long id) {

    }

    @Override
    public OrderResponse customerOrders(OrderRequest orderRequest) {

        customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new IllegalStateException("Customer not found"));

        productClient.getProduct(orderRequest.getProductId());

        OrderResponse orderResponse = orderClient.createOrder(orderRequest);

        return orderResponse;
    }

    @Override
    public PaymentResponse customerPayment(PaymentRequest paymentRequest) {
        customerRepository.findById(paymentRequest.getCustomerId())
                .orElseThrow(() -> new IllegalStateException(CUSTOMER_NOT_FOUND_EXCEPTION));

        return paymentClient.createPayment(paymentRequest);
    }
}
