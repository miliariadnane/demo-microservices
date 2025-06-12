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
import dev.nano.exceptionhandler.business.CustomerException;
import dev.nano.exceptionhandler.business.NotificationException;
import dev.nano.exceptionhandler.business.OrderException;
import dev.nano.exceptionhandler.business.PaymentException;
import dev.nano.exceptionhandler.core.DuplicateResourceException;
import dev.nano.exceptionhandler.core.ResourceNotFoundException;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

import static dev.nano.customer.CustomerConstant.*;

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

    public boolean isSameUser(Authentication authentication, Long customerId) {
        String username = authentication.getName();
        CustomerEntity customer = customerRepository.findById(customerId).orElse(null);
        return customer != null && customer.getEmail().equals(username);
    }

    @Override
    public CustomerDTO getCustomer(Long id) {
        return customerRepository.findById(id)
                .map(customerMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(CUSTOMER_NOT_FOUND, id)
                ));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<CustomerEntity> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            throw new ResourceNotFoundException(NO_CUSTOMERS_FOUND);
        }
        return customerMapper.toListDTO(customers);
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        validateNewCustomer(customerDTO);
        try {
            CustomerEntity customer = customerMapper.toEntity(customerDTO);
            CustomerEntity savedCustomer = customerRepository.save(customer);
            sendWelcomeNotification(savedCustomer);
            log.info("Customer created successfully: {}", savedCustomer);
            return customerMapper.toDTO(savedCustomer);
        } catch (Exception e) {
            throw new CustomerException("Failed to create customer: %s" + e.getMessage());
        }
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        CustomerEntity existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(CUSTOMER_NOT_FOUND, id)
                ));

        if (!existingCustomer.getEmail().equals(customerDTO.getEmail())
                && customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new DuplicateResourceException(
                    String.format(CUSTOMER_EMAIL_EXISTS, customerDTO.getEmail())
            );
        }

        customerMapper.updateCustomerFromDTO(customerDTO, existingCustomer);
        return customerMapper.toDTO(customerRepository.save(existingCustomer));
    }


    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    String.format(CUSTOMER_NOT_FOUND, id)
            );
        }
        try {
            customerRepository.deleteById(id);
        } catch (Exception e) {
            throw new CustomerException("Failed to delete customer: " + e.getMessage());
        }
    }

    @Override
    public OrderResponse customerOrders(OrderRequest orderRequest) {
        customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(CUSTOMER_NOT_FOUND, orderRequest.getCustomerId())
                ));

        try {
            productClient.getProduct(orderRequest.getProductId());
            return orderClient.createOrder(orderRequest);
        } catch (FeignException e) {
            throw new OrderException("Failed to process order: " + e.getMessage());
        }
    }

    @Override
    public PaymentResponse customerPayment(PaymentRequest paymentRequest) {
        customerRepository.findById(paymentRequest.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(CUSTOMER_NOT_FOUND, paymentRequest.getCustomerId())
                ));

        try {
            return paymentClient.createPayment(paymentRequest);
        } catch (FeignException e) {
            throw new PaymentException("Failed to process payment: " + e.getMessage());
        }
    }

    private void validateNewCustomer(CustomerDTO customerDTO) {
        if (customerRepository.existsByEmail(customerDTO.getEmail())) {
            throw new DuplicateResourceException(
                    String.format(CUSTOMER_EMAIL_EXISTS, customerDTO.getEmail())
            );
        }
    }

    private void sendWelcomeNotification(CustomerEntity customer) {
        try {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .customerId(customer.getId())
                    .customerName(customer.getName())
                    .customerEmail(customer.getEmail())
                    .sender("nanodev")
                    .message("Welcome to nanodev demo project on microservices")
                    .build();

            rabbitMQProducer.publish(
                    "internal.exchange",
                    "internal.notification.routing-key",
                    notificationRequest
            );
        } catch (Exception e) {
            log.error("Failed to send welcome notification: {}", e.getMessage());
            throw new NotificationException("Failed to send welcome notification");
        }
    }
}
