package dev.nano.customer;

import dev.nano.clients.order.OrderRequest;
import dev.nano.clients.order.OrderResponse;
import dev.nano.clients.payment.PaymentRequest;
import dev.nano.clients.payment.PaymentResponse;

import java.util.List;

public interface CustomerService {

    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;
    List<CustomerDTO> getAllCustomers();
    CustomerDTO createCustomer(CustomerDTO customer);
    CustomerDTO updateCustomer(Long id, CustomerDTO customer);
    void deleteCustomer(Long id);
    OrderResponse customerOrders(OrderRequest orderRequest);
    PaymentResponse customerPayment(PaymentRequest paymentRequest);
}
