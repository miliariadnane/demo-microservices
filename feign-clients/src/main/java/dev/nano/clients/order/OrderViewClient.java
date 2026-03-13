package dev.nano.clients.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-view", url = "${clients.order.url}")
public interface OrderViewClient {

    @GetMapping(path = "/api/v1/orders/views/{orderId}")
    OrderViewResponse getOrderView(@PathVariable("orderId") Long orderId);

    @GetMapping(path = "/api/v1/orders/views/list")
    List<OrderViewResponse> getAllOrderViews();

    @GetMapping(path = "/api/v1/orders/views/customer/{customerId}")
    List<OrderViewResponse> getOrderViewsByCustomer(@PathVariable("customerId") Long customerId);
}
