package dev.nano.clients.customer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer", url = "${clients.customer.url}")
public interface CustomerClient {

    @GetMapping(path = "/api/v1/customers/{customerId}")
    CustomerResponse getCustomer(@PathVariable("customerId") Long customerId);
}
